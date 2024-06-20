package com.yc.beanVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 搜索页面的VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneBrandVO {
    //pid,name,brand,pics,price,num
    private Integer num;
    private Integer pid;
    private String pics;
    private String name;
    private String price;
    private String brand;



}
