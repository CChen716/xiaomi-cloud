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
@TableName("orderinfo")
public class OrderInfo  implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer ono;
    private String odate; // datetime类型
    private String status;
    private Integer uid;
    private Double price;

    private Integer aid;
}
