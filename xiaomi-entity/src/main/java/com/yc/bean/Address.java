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
@TableName("tb_address")
public class Address implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer aid;
    private Integer uid;
    private String address;
    private String tel;
    private String consignee;
}
