package com.yc.biz;

import com.yc.bean.Address;
import com.yc.bean.OrderInfo;
import com.yc.bean.OrderItemInfo;
import com.yc.beanVO.OrderShowInfo;
import com.yc.config.SeckillConfig;
import com.yc.dao.OrderDao;
import com.yc.dao.OrderIteminfoDAO;

import com.yc.util.CacheService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS,
        isolation = Isolation.DEFAULT, timeout = 2000,
        readOnly = true, rollbackFor = RuntimeException.class)
@Slf4j
public class OrderBizImpl implements OrderBiz{
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderIteminfoDAO orderIteminfoDAO;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final DefaultRedisScript<String> SECKILL_SCRIPT;
    //静态块初始化
    static {
        SECKILL_SCRIPT=new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua")); //路径位置
        SECKILL_SCRIPT.setResultType(String.class);  //返回结果类型
    }


    /**
     * 下订业务
     * @param orderInfo
     * @return
     */
    @Override
    public OrderInfo addOrderInfo(OrderInfo orderInfo) {
        //将订单信息写入
        this.orderDao.insert(orderInfo);
        //然后直接返回写入的购物车订单信息传入结算页面
        return this.orderDao.findnew(orderInfo);
    }

    //此方法失效

    /**
     * 加入分布式锁解决Redis集群并发 库存扣减问题
     * 加入rabbitMQ 实现异步处理
     *
     * @param pid  商品id
     * @param num 数量
     * @return
     */
    @Override
    public Map<String, Object> addOrderIteminfo(String pid,String num) {
        Map<String, Object> map=new HashMap<>();
        long threadId = Thread.currentThread().getId();//线程id作为key
        String key= SeckillConfig.LockKey+threadId;//锁的key
        RLock lock = redissonClient.getLock(key);
        boolean isLock = lock.tryLock(); //获取锁
        if (!isLock){ //如果锁获取失败
            log.info("当前线程锁获取失败");
            map.put("code",0);
            return map;//失败处理
        }
        //TODO 将订单写入到数据库中----使用rabbitMQ异步处理
        try {
            //执行lua脚本 实现数据查询和库存扣减  此处完成Redis库存扣减
            String result = redisTemplate.execute(SECKILL_SCRIPT, Collections.singletonList(key), num);
            log.info("lua脚本运行结果："+result);
            if (result.equals("0")){//0表示出现下订单失败
                map.put("code",0); //失败处理
                return map;
            }else {//表示库存扣减成功
                //TODO 1.将订单写入订单详情表 插入订单     交由MQ异步处理
                //TODO 2.将扣减的库存更新商品详情表        交由MQ异步处理
                //TODO 同步到ElasticSearch  --MQ     这一步通过Canal+MQ  不在此处处理

                map.put("code",1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (lock.isLocked()&lock.isHeldByCurrentThread()){
                lock.unlock();//释放锁
            }
        }
        return map;
    }

    @Override
    public void updatePhoneinfo(int []gnos, int []nums) {
        for (int i = 0; i < gnos.length; i++) {
            int gno = gnos[i];
            int num = nums[i];
            this.orderDao.updatePhoneinfo(gno, num);
        }
    }

    @Override
    public List<Map<String, List<OrderShowInfo>>> findByOno(int ono) {
        return this.orderDao.findByOno(ono);
    }

    @Override
    public List<Map<String, List<Address>>> findAdd(int uid) {
        return this.orderDao.findAdd(uid);
    }

    @Override
    public void updateAdd(int ono, int aid) {
        this.orderDao.updateAdd(ono,aid);
    }
}
