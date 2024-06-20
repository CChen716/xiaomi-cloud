package com.yc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsPhone {
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
