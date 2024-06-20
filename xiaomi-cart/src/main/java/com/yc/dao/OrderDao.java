package com.yc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.bean.Address;
import com.yc.bean.OrderInfo;
import com.yc.beanVO.OrderShowInfo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface OrderDao extends BaseMapper<OrderInfo> {
    @Select(" select o.gno,o.ino,u.name, g.color,g.cap,g.price,o.num,g.pics\n" +
            " from tb_phoneinfo g,tb_phone u,orderiteminfo o \n" +
            " where o.ono=#{ono} and o.gno=g.id and g.pid=u.pid; ")
    List<Map<String, List<OrderShowInfo>>> findByOno(int ono);

    //查询地址sql
    @Select(" select aid,address,tel,consignee\n" +
            " from tb_address where uid=#{uid}; ")
    List<Map<String, List<Address>>> findAdd(int uid);

    //扣减库存sql
    @Update("update tb_phoneinfo set num = num - #{num} where id = #{gno}")
    void updatePhoneinfo(int gno, int num);

    //为什么tnd要这样子写  不是有用户id吗 tnd不是可以根据uid查询吗
    @Select("SELECT * FROM orderinfo ORDER BY ono DESC LIMIT 1;")
    OrderInfo findnew(OrderInfo orderInfo);

    @Update("update orderInfo set aid = #{aid} where ono = #{ono}")
    void updateAdd(int ono, int aid);

}
