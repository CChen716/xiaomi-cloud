package com.yc.xiaomi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.bean.OrderInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface OrderDao extends BaseMapper<OrderInfo> {
    //查询有效订单的信息
    @Select(" select o.ono,o.status,oi.statu,oi.num,pi.pics,oi.price,a.aid,a.address,a.consignee,a.tel,u.username,oi.ino,o.odate" +
            ",p.brand,p.name,pi.color,pi.cap " +
            " from orderiteminfo oi,orderinfo o,user u,tb_phone p,tb_phoneinfo pi,tb_address a " +
            " where oi.ono=o.ono and o.uid=u.id and o.aid=a.aid and o.aid=a.aid and pi.id=oi.gno and p.pid=pi.pid and oi.statu=1 and u.id=#{id} " +
            " limit #{start},#{pagesize}")
    List<Map<String,Object>> selectAllOrder(Integer id,int start,int pagesize);

    //查询未收货订单的信息
    @Select(" select o.ono,o.status,oi.statu,oi.num,pi.pics,oi.price,a.aid,a.address,a.consignee,a.tel,u.username,oi.ino,o.odate" +
            ",p.brand,p.name,pi.color,pi.cap " +
            " from orderiteminfo oi,orderinfo o,user u,tb_phone p,tb_phoneinfo pi,tb_address a " +
            " where oi.ono=o.ono and o.uid=u.id and o.aid=a.aid and pi.id=oi.gno and p.pid=pi.pid and oi.statu=1 and o.status=2 and u.id=#{id} " +
            " limit #{start},#{pagesize}")
    List<Map<String,Object>> selectUnReceiveOrder(Integer id,int start,int pagesize);

    //查询已收货订单的信息
    @Select(" select o.ono,o.status,oi.statu,oi.num,pi.pics,oi.price,a.aid,a.address,a.consignee,a.tel,u.username,oi.ino,o.odate" +
            ",p.brand,p.name,pi.color,pi.cap " +
            " from orderiteminfo oi,orderinfo o,user u,tb_phone p,tb_phoneinfo pi,tb_address a " +
            " where oi.ono=o.ono and o.uid=u.id and o.aid=a.aid and pi.id=oi.gno and p.pid=pi.pid and oi.statu=1 and o.status=3 and u.id=#{id} " +
            " limit #{start},#{pagesize}")
    List<Map<String,Object>> selectReceiveOrder(Integer id,int start,int pagesize);


    //查询关闭订单的信息
    @Select(" select o.ono,o.status,oi.statu,oi.num,pi.pics,oi.price,a.aid,a.address,a.consignee,a.tel,u.username,oi.ino,o.odate" +
            ",p.brand,p.name,pi.color,pi.cap " +
            " from orderiteminfo oi,orderinfo o,user u,tb_phone p,tb_phoneinfo pi,tb_address a " +
            " where oi.ono=o.ono and o.uid=u.id and o.aid=a.aid and pi.id=oi.gno and p.pid=pi.pid and oi.statu=0  and u.id=#{id} " +
            " limit #{start},#{pagesize}")
    List<Map<String,Object>> selectCloseOrder(Integer id,int start,int pagesize);

    //查询指定编号的订单
    @Select(" select o.ono,o.status,oi.statu,oi.num,pi.pics,oi.price,a.aid,a.address,a.consignee,a.tel,u.username,oi.ino,o.odate" +
            ",p.brand,p.name,pi.color,pi.cap " +
            " from orderiteminfo oi,orderinfo o,user u,tb_phone p,tb_phoneinfo pi,tb_address a" +
            " where oi.ono=o.ono and o.uid=u.id and o.aid=a.aid and pi.id=oi.gno and p.pid=pi.pid  and u.id=#{id} and " +
            " oi.ino=#{ino}")
    Map<String,Object> selectOnlyOrder(Integer id,Integer ino);

    //根据商品名称或订单号查找订单
    @Select(" select o.ono,o.status,oi.statu,oi.num,pi.pics,oi.price,a.aid,a.address,a.consignee,a.tel,u.username,oi.ino,o.odate" +
            ",p.brand,p.name,pi.color,pi.cap " +
            " from orderiteminfo oi,orderinfo o,user u,tb_phone p,tb_phoneinfo pi,tb_address a " +
            " where oi.ono=o.ono and o.uid=u.id and o.aid=a.aid and pi.id=oi.gno and p.pid=pi.pid  and u.id=#{id}  " +
            " and (o.ono='${message}' or p.name like '%${message}%') limit #{start},#{pagesize}")
    List<Map<String,Object>> selectByInoOrName(Integer id,String message,int start,int pagesize);
}
