package com.yc.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




@Configuration
public class RabbitMQTopicConfig {
    //创建队列
    private  static final  String QUEUE="seckillQueue";
    //创建交换机
    private static  final  String EXCHANGE="seckillExchange";
    @Bean
    public Queue queue(){
        return  new Queue(QUEUE);
    }
    //匹配规则
    @Bean
    public TopicExchange topicExchange(){
        return  new TopicExchange(EXCHANGE);
    }
    @Bean
    public Binding binging(){
        //创建队列和路由和路由键
        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");
    }


    @Bean
    public MessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }




}