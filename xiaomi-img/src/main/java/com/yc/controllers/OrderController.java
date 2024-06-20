package com.yc.controllers;


import com.yc.biz.OrderBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderBiz orderBiz;
    @RequestMapping(value = "findOrderByDate",method={RequestMethod.GET})
    public Map<String,Object>  findOrderByDate( ){
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTimeStr = sdf.format(now);
        Map<String, Object> orderByDate = orderBiz.findOrderByDate(currentDateTimeStr);
        return orderByDate;


    }
}