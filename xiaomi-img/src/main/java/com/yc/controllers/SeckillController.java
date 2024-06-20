package com.yc.controllers;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yc.bean.SeckillGoods;
import com.yc.bean.SeckillMessage;
import com.yc.bean.SeckillOrder;
import com.yc.bean.User;
import com.yc.biz.MQSender;
import com.yc.biz.SeckillGoodsBiz;
import com.yc.biz.SeckillOrderBiz;

import com.yc.util.InterceptorReq;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;


import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("seckill")
public class SeckillController implements InitializingBean {
    @Autowired
    private SeckillGoodsBiz seckillGoodsBiz;
    @Autowired
    private SeckillOrderBiz seckillOrderBiz;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqSender;
    @RequestMapping("findSeckillByGoodId")
    public  Map<String,Object> findSeckillByGoodId(){
        Map<String,Object> result=new HashMap<>();
        SeckillGoods seckillByGood = seckillGoodsBiz.findSeckillByGoodId(1);
        if (seckillByGood==null){

            result.put("code",0);
            result.put("msg","暂无优惠卷");
            return result;

        }
        result.put("code",1);
        result.put("data",seckillByGood);
        return result;


    }
    @RequestMapping("showDiscount")
    public Map<String,Object> findDiscount(@RequestParam Integer userid,@RequestParam Integer goodsid){
        Map<String,Object > result=new HashMap<>();
        SeckillOrder seckillOrder = seckillOrderBiz.findDiscount(userid, goodsid);
        if (seckillOrder!=null){
            result.put("code",1);
            result.put("data",seckillOrder);

        }else{
            result.put("code",0);
            result.put("data",1);
        }
        return result;
    }
    //获取秒杀的结果
    @RequestMapping(value = "getSeckillResult",method = {RequestMethod.POST})
    public Map<String,Object> getSeckillResult( @RequestParam Integer userid, @RequestParam Integer goodsId){
        Map<String,Object> result=new HashMap<>();
        String seckillResult = seckillOrderBiz.getSeckillResult(1, 1);
        if ("-1".equals(seckillResult)){
            result.put("code",-1);
            result.put("data","票已经抢完");
            return result ;
        }else if ("0".equals(seckillResult)){
            result.put("code",0);
            result.put("data","还在排队中,请稍后再试");
            return result;

        }else{
            result.put("code",1);
            result.put("data","恭喜你抢票成功");
            return result;
        }
    }


    private Map<Integer,Boolean> EmptyStockMap=new HashMap<>();
    @RequestMapping(value = "doSeckill",method={RequestMethod.POST} )
    @InterceptorReq(clazz = "com.yc.controller.SeckillController",overdue = 5000)
    public Map<String,Object> doSeckill(@RequestParam Integer goodsId, HttpSession session){
        Map<String,Object>  result=new HashMap();
        User user1=new User();
        user1.setId(1);
        session.setAttribute("user",user1);
        if (session.getAttribute("user")==null){
            result.put("code",0);
            result.put("msg","用户未登录");
            return  result;
        }
        if (EmptyStockMap.get(goodsId)){
            //减少当库存不足,访问redis
            result.put("code",0);
            result.put("msg","库存不足");
            return  result;

        }
        //判断库存是否有
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //获取的是递减之后的库存
        Long stock=valueOperations.decrement("seckillGoods:"+goodsId);
        if (stock<0){
            EmptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods:"+goodsId);
            result.put("code",0);
            result.put("msg","库存不足");
            return result;
        }
//        SeckillGoods seckillGoods = seckillGoodsBiz.findSeckillGoodsByGoodsid(goodsId);
//        if (seckillGoods.getStockcount()<1){
//            result.put("code",0);
//            result.put("msg","库存不足");
//            return result;
//
//        }
        User user= (User) session.getAttribute("user");
        //判断用户是否重复抢,根据秒杀订单表中用户id和商品id是否重复
//      优化从redis中获取是否从抢购
        SeckillOrder seckillOrder= (SeckillOrder) redisTemplate.opsForValue().get("seckillOrder:"+user.getId()+":"+goodsId);

        if(seckillOrder!=null){
            result.put("code",0);
            result.put("msg","你已经重复购买");
            return result;
        }
        //使用rabbitmq进行异步操作的有助于前期肖峰
//        SeckillOrder seckill = seckillOrderBiz.seckill(user, seckillGoods)
        SeckillMessage seckillMessage=new SeckillMessage(user,goodsId);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            mqSender.sendSeckillMessage(objectMapper.writeValueAsString(seckillMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.put("code",1);
        //返回的值为零要轮询
        result.put("data",0);
        return result;
    }
    //完成bean生命周期的
    @Override
    public void afterPropertiesSet() throws Exception {
        List<SeckillGoods> list=seckillGoodsBiz.findSeckillGoods();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        list.forEach(seckillGoods->{

            redisTemplate.opsForValue().set("seckillGoods:"+seckillGoods.getGoodsid(),seckillGoods.getStockcount());
            EmptyStockMap.put(seckillGoods.getGoodsid(),false);

        });

    }
}
