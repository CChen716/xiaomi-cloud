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
@TableName("orderiteminfo")
public class OrderItemInfo  implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer ino;
    private String ono;
    private Integer gno;
    private Integer num;
    private Double price;
    private Integer statu;
}
