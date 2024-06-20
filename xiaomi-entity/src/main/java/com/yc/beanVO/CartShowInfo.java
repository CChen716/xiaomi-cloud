package com.yc.beanVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartShowInfo implements Serializable {
    private String brand;
    private String name;
    private String color;
    private String cap;
    private String price;
    private String cno;
    private Integer num;
    private String pics;
}
