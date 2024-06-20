package com.yc.bean;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_phone")
public class Phone  implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer pid;
    private String name;
    private String brand;


    @TableField(exist = false)
    private String color;
    @TableField(exist = false)
    private Integer num;
    @TableField(exist = false)
    private Double price;
    @TableField(exist = false)
    private String pics;
    @TableField(exist = false)
    private String cap;

}
