package com.yc.dao;

import com.alibaba.nacos.shaded.com.google.common.collect.Ordering;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.bean.OrderInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface OrderDao extends BaseMapper<OrderInfo> {
    @Select("SELECT ono,odate,status,price,uid,aid " +
            "FROM `orderinfo` " +
            "WHERE status != 0 AND DATE(odate) = #{date}")
    public List<OrderInfo> findByOdate(String date);




}