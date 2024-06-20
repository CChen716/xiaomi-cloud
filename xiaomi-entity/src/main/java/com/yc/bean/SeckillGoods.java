package com.yc.bean;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_seckil_goods")
public class SeckillGoods {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer goodsid;
    private Double  seckillprice;
    private Integer stockcount;
    private String startdate;
    private String enddate;
}