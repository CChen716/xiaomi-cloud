package com.yc.util.mq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;

/**  这样就不用再发送延迟消息器里面创建对象了
 * @author 12547
 * @version 1.0
 * @Date 2024/3/18 20:20
 */
@RequiredArgsConstructor //@RequiredArgsConstructor 注解可以用于类上，用于自动生成一个包含所有被 final 修饰的成员变量
public class DelayPostMessage implements MessagePostProcessor {

    private final int delay;

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        message.getMessageProperties().setDelay(delay);
        return message;
    }
}
