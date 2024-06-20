package com.patterns.chain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 订单类
 * @author cg
 * @version 1.0
 * @Date 2024/6/13 15:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String orderId;  //订单id

    private Integer money;  //订单金额

}
