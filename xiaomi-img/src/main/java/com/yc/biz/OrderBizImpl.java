package com.yc.biz;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yc.bean.OrderInfo;
import com.yc.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderBizImpl implements OrderBiz {
    @Autowired
    private OrderDao orderDao;
    @Override
    public Map<String,Object> findOrderByDate(String date) {
        Map<String,Object> result=new HashMap<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        //今日的销售量
        List<OrderInfo> todaySales = orderDao.findByOdate(date);
        //表示今日销售量为零
        Double todaysales=0.0;
        Double yesterdaysales=0.0;
        Double pretweeksales=0.0;
        if (todaySales==null){

        }else{
            for(OrderInfo item : todaySales ){
                todaysales+=item.getPrice();
            }
        }


        try {
            Date   today=sdf.parse(date);
            Calendar instance = Calendar.getInstance();
            instance.setTime(today);
            instance.add(Calendar.DAY_OF_MONTH,-1);
            //昨天的销售量
            String yesterday =sdf.format(instance.getTime());
            //昨日的销售量
            List<OrderInfo> yesterdaySales = orderDao.findByOdate(yesterday);
            //表示今日销售量为零

            if (yesterdaysales==null){

            }else{
                for(OrderInfo item : yesterdaySales ){
                    yesterdaysales+=item.getPrice();
                }
            }

            //上一周的
            Calendar instance2 = Calendar.getInstance();
            instance.setTime(today);
            instance.add(Calendar.DAY_OF_MONTH,-7);
            String pretweek =sdf.format(instance2.getTime());
            //昨日的销售量
            List<OrderInfo> pretweekSales = orderDao.findByOdate(pretweek);
            //表示上一周销售量为零

            if (pretweekSales==null){

            }else{
                for(OrderInfo item : yesterdaySales ){
                    pretweeksales+=item.getPrice();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        result.put("sales",todaysales);
        result.put("yessales",yesterdaysales);
        result.put("preweeksales",pretweeksales);
        return result;
    }
}