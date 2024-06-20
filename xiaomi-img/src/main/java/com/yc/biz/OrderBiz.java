package com.yc.biz;

import com.yc.bean.OrderInfo;

import java.util.List;
import java.util.Map;

public interface OrderBiz {
    Map<String,Object> findOrderByDate(String date);
}