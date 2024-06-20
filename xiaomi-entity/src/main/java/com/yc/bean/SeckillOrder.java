package com.yc.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_seckil_order")
public class SeckillOrder {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userid;
    private Integer goodsid;
    @TableField(exist = false)
    private Double seckillprice;
}