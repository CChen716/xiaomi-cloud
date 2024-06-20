package com.yc.beanVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneManageVO {
    private Integer id;
    private Integer pid;
    private String color;
    private Integer num;
    private Double price;
    private String pics;
    private String cap;
    private String name;
    private String brand;
}
