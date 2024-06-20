package com.yc.mqlistener;

import com.yc.config.MqConstants;
import com.yc.util.CacheService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**异步数据更新Redis类
 * @author 12547
 * @version 1.0
 * @Date 2024/3/20 15:49
 */
@Component
public class RedisDataListenerService {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * Redis数据更新消费者监听方法
     */
    @RabbitListener(queues = MqConstants.QUEUE_NAME)
    public void updateRedisDataByAsync(Map<String,Object> msg){
        System.out.println("监听到数据变化：");
        System.out.println("数据变化商品id:"+msg.get("id"));  //正常情况Redis应该每个商品id一个key  TODO 需要改造详情缓存查询将List<phone>改为单独的一个phone对象
    //    redisTemplate.opsForHash().putAll(msg.get("id").toString(),msg);  //更新Redis数据
        cacheService.addHashCacheAll(msg.get("id").toString(),msg,300L, TimeUnit.MINUTES);  //5小时过期时间
        System.out.println("更改后的库存数："+cacheService.getHashCache(msg.get("id").toString(), "num"));
    }

}
