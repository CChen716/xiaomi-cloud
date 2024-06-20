package com.yc.biz;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yc.bean.SeckillGoods;
import com.yc.bean.SeckillMessage;
import com.yc.bean.SeckillOrder;
import com.yc.bean.User;
import com.yc.biz.SeckillGoodsBiz;
import com.yc.biz.SeckillOrderBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MQReceiver {
    @Autowired
    private SeckillGoodsBiz seckillGoodsBiz;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillOrderBiz seckillOrderBiz;

    @RabbitListener(queues="seckillQueue")
    public void receive(String msg) throws IOException {
        Map<String,Object> result=new HashMap<>();
        log.info("接受的消息:"+msg);
        // 创建 ObjectMapper 对象
        ObjectMapper objectMapper = new ObjectMapper();

// 将 JSON 字符串转换为对象
        SeckillMessage seckillMessage = objectMapper.readValue(msg, SeckillMessage.class);
        Integer goodsId=seckillMessage.getGoodsid();
        User user=seckillMessage.getUser();
        //检查库存是否
        SeckillGoods seckillGoods = seckillGoodsBiz.findSeckillGoodsByGoodsid(goodsId);
        if (seckillGoods.getStockcount()<1){
            result.put("code",0);
            result.put("msg","库存不足");
            return;
        }

        //判断用户是否重复抢,根据秒杀订单表中用户id和商品id是否重复
//      优化从redis中获取是否从抢购
        //此处修改了强制启动
        SeckillOrder seckillOrder= (SeckillOrder) redisTemplate.opsForValue().get("seckillOrder:"+user.getId()+":"+goodsId);
        if(seckillOrder!=null){
            result.put("code",0);
            result.put("msg","你已经重复购买");
            return ;
        }
        //下单成功

        seckillOrderBiz.seckill(user, seckillGoods);

    }
}