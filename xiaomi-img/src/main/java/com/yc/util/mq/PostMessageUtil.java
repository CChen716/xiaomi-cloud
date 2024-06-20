package com.yc.util.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**封装的MQ消息发送类
 * @author 12547
 * @version 1.0
 * @Date 2024/3/19 17:23
 */
public class PostMessageUtil {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //发送消息
    public void sendMessageToMq(){

    }




}
