package com.yc.mqlisteners;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**  Mysql同步数据至ES监听类
 * @author 12547
 * @version 1.0
 * @Date 2024/3/13 15:45
 */
@Component
public class MqListener {

  //TODO 使用ES操作业务类实现更新(crud)


}
