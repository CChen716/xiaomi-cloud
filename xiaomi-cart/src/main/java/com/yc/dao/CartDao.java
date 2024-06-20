package com.yc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.bean.CartInfo;
import com.yc.bean.OrderItemInfo;
import com.yc.bean.PhoneInfo;
import com.yc.beanVO.CartShowInfo;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface CartDao extends BaseMapper<CartInfo> {
    @Select(" select u.brand,u.name,g.pics,g.color,g.cap,g.price,c.cno, " +
            " c.num from tb_phoneinfo g,cartinfo c,tb_phone u " +
            " where c.uno=#{uno} and c.gno=g.id and g.pid=u.pid; ")
    List<Map<String, List<CartShowInfo>>> findByUno(int uno);

    @Select("select sum(num) from cartinfo where uno = #{uno}")
    int totalCartNum(int uno);

    @Select("update cartinfo set num = num+#{num} where cno=#{cno}")
    void update(int num,int cno);

    @Select("insert into cartinfo values(#{cno},#{uno},#{gno},#{num})")
    List<CartInfo> add(CartInfo cart);

    @Select(" select cno,uno,gno,num from cartinfo where cno=#{cno}; ")
    CartInfo findByCno(int cno);

    @Select(" select id,pid,color,num,price,price,pics,cap from tb_phoneinfo where id=#{gno}; ")
    PhoneInfo findByGno(int gno);

    @Select(" INSERT INTO orderiteminfo (ono, gno, num, price, statu) VALUES (#{ono},#{gno}, #{num},#{price}, #{statu}); ")
    OrderItemInfo addorderIteminfo(OrderItemInfo orderItemInfo);
}
