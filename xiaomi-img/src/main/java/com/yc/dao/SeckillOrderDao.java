package com.yc.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.bean.SeckillOrder;
import org.apache.ibatis.annotations.Select;

public interface SeckillOrderDao extends BaseMapper<SeckillOrder> {

    @Select("select a.id,userid,a.goodsid,b.seckillprice from tb_seckil_order a inner join tb_seckil_goods b " +
            " on a.goodsid=b.goodsid" +
            " where a.userid = #{userid} and a.goodsid=#{goodsid}")
    public SeckillOrder findDiscount(Integer userid,Integer goodsid);

}