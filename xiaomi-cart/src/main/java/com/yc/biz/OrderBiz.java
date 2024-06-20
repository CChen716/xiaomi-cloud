package com.yc.biz;

import com.yc.bean.Address;
import com.yc.bean.OrderInfo;
import com.yc.bean.OrderItemInfo;
import com.yc.beanVO.OrderShowInfo;


import java.util.List;
import java.util.Map;

public interface OrderBiz {
    public OrderInfo addOrderInfo(OrderInfo orderInfo);
    public Map<String,Object> addOrderIteminfo(String pid,String num);
    public void updatePhoneinfo(int []gnos, int []nums);
    public List<Map<String, List<OrderShowInfo>>>  findByOno(int ono);
    public List<Map<String, List<Address>>> findAdd(int uid);
    public void updateAdd(int ono ,int aid);
}
