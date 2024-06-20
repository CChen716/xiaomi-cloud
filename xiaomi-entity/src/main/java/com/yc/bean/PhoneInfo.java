package com.yc.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_phoneinfo")
public class PhoneInfo  implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer pid;
    private String color;
    private Integer num;
    private String price;
    private String pics;
    private String cap;


}
