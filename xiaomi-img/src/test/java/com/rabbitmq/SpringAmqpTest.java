package com.rabbitmq;

import com.yc.ImgApp;
import com.yc.util.mq.DelayPostMessage;
import com.yc.util.mq.MultiDelayMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * @author 12547
 * @version 1.0
 * @Date 2024/3/13 15:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ImgApp.class)
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Test
    public void testSendMq(){
        //队列名称
        String que="test.queue";
        //消息
        String msg="hello,test";

        rabbitTemplate.convertAndSend(que,msg);
    }

    @Test
    public void testSendfanoutMq(){
        //队列名称
        String exchangeName="xiaomi.fanout";
        //消息
        String msg="hello,fanout";

        rabbitTemplate.convertAndSend(exchangeName,null,msg);
    }

    @Test
    public void testSendTopicMq(){
        //队列名称
        String exchangeName="xiaomi.topic";
        //消息
        String msg="hello,topic";

        rabbitTemplate.convertAndSend(exchangeName,"janpans.news",msg);
    }

    @Test
    public void testSendTopicMapMsg(){
        //队列名称
        String exchangeName="xiaomi.topic";
        //消息
       // String msg="hello,topic";

        //对象消息 并且映入fasterjackson
        HashMap<String, Object> map = new HashMap<>();
        map.put("哈哈哈","测试注解消费者创建交换机以及队列topic方式");

        rabbitTemplate.convertAndSend(exchangeName,"test.news",map);
    }

    //测试延时消息
    @Test
    public void testSendDelayMapMsg(){
        //队列名称

        //消息
         String msg="hello,delay";

        rabbitTemplate.convertAndSend("delay.direct", "delay", msg, new MessagePostProcessor() {

            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //添加延时消息属性
                message.getMessageProperties().setDelay(5000);  //设置延时时间5s
                return message;
            }
        });
        System.out.println("消息发送成功"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    //测试自定延迟消息
    @Test
    public void testSendMyDelayMapMsg(){

        //消息
        String mymsg="hello,Mydelay";
        MultiDelayMessage<String> msg=MultiDelayMessage.of(mymsg,100L,200L,300L);


        rabbitTemplate.convertAndSend("delay.direct", "delay", msg, new DelayPostMessage(msg.removeNextDelay().intValue()));

        System.out.println("消息发送成功"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

}
