package com.yc.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yc.bean.Com;
import com.yc.bean.Phone;
import com.yc.dao.ComDao;
import com.yc.util.CacheService;
import com.yc.util.JedisClientSingleton;
import com.yc.util.JedisUtil;
import io.rebloom.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.sql.Wrapper;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ComBizImpl implements ComBiz {

    private final int EXPIRE_TIME = 1440;   //60*24 分钟  表示一天
    private final TimeUnit EXPIRE_TIME_TYPE = TimeUnit.MINUTES;  //s单位分钟

    @Autowired
    private ComDao comDao;

    @Autowired
    private CacheService cacheService;  //Redis业务类

    @Autowired
    private JedisUtil jedisUtil; //jedis封装线程池操作类





    public Map<Integer, List<Com>> findComByForeignId(Integer pid ){

        List<Com> coms = this.comDao.selectByForeignId(pid);
        Map<Integer,List<Com>>map=new HashMap<>();
        if (coms==null||coms.size()<0){
            return  map;

        }
        for(Com com:coms){
            Integer parentid=com.getPid();
            if(map.containsKey(parentid)){
                List<Com> sonList=map.get(parentid);
                sonList.add(com);
            }else{
                List<Com>sonList=new ArrayList<>();
                sonList.add(com);
                map.put(parentid,sonList);
            }
        }
        return  map;
    }

    @Override
    public int addCom(Com com) {
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-dd");
        String dString=sdf.format(date);
        com.setCreatetime(dString);
        return this.comDao.insert(com);

    }

    /**
     * 已加入布隆过滤器 以及测试单例模式  key=phoneid   防止缓存穿透
     * 已设置随机数   防止缓存雪崩
     * @param pid
     * @return
     */
    //详情业务    加入Redis
    @Override
    public List<Phone> findPhoneByPid(Integer pid) {
        List<Phone> list=null;
        Client client=JedisClientSingleton.getInstance(); //利用双重检查所确保client对象单例
        //防止缓存穿透 先从           布隆过滤器中查询--->redis---->DB     未查询到则直接返回
        if(client.exists("phoneid", String.valueOf(pid))){  //布隆过滤器判断 如果不存在 则一定不存在于缓存和DB中 直接返回
            if (this.cacheService.hasKey(String.valueOf(pid))) {    //hasKey(key) 判断是否存在该key
                list=this.cacheService.getList(pid,Phone.class);   //如果存在该Key 则从Redis中拿取
            }else {
                Random random=new Random();  //生成随机数 分散缓存过期时间 防止缓存雪崩
                int time=random.nextInt(100);//随机[1,100]分钟
                list=comDao.selectPhoneByPid(pid);   //否则走DB 从数据库中拿取
                this.cacheService.add(pid,list,EXPIRE_TIME+time, EXPIRE_TIME_TYPE);     // 然后将该List数据存入Redis 并设置过期时间  TODO 过期时间应该随机设置--防止缓存雪崩
                //那么下次则会走Redis 而不会走DB
            }
            return list;
        }
        return null; //不存在直接返回空
    }

}