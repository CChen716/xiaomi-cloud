package com.yc.biz;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    //发送秒杀信息
    public void sendSeckillMessage(String message){
        log.info("发送的消息"+message);
        rabbitTemplate.convertAndSend("seckillExchange","seckill.message",message);

    }

}
