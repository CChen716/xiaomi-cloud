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
@TableName("cartinfo")
public class CartInfo  implements Serializable {  //若不序列化则无法存入redis
    @TableId(type = IdType.AUTO)
    private Integer cno;
    private Integer uno;
    private Integer gno;
    private Integer num;

}
