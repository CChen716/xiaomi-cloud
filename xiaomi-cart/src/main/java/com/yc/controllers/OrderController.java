package com.yc.controllers;

import com.yc.bean.Address;
import com.yc.bean.OrderInfo;
import com.yc.bean.OrderItemInfo;
import com.yc.beanVO.OrderShowInfo;
import com.yc.biz.OrderBiz;

import com.yc.config.SeckillConfig;
import com.yc.util.CacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("order")
@Slf4j
@Api(tags = "订单")
public class OrderController {
    @Autowired
    private OrderBiz orderBiz;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CacheService cacheService;


    /**下订方法
     *
     *问题：下订单之前是否需要检查商品库存  如果是秒杀环境下   单台解决方案使用Lua脚本-----单台redis情况下
     *
     *这个方法是在购物车界面点击去结算调用
     *目前只能在此进行分布式锁实现
     *
     * */
    @RequestMapping(value ="addOrderInfo" ,method = {RequestMethod.POST})
    @ApiOperation(value = "添加订单")
    public Map<String, Object> addOrderInfo(@RequestParam String odate,
                                            @RequestParam String status,
                                            @RequestParam int uid,
                                            @RequestParam double price,
                                            @RequestParam int aid) {
        Map<String, Object> map = new HashMap<>();
        OrderInfo orderInfo = new OrderInfo();

        LocalDateTime now = LocalDateTime.now();
        //创建DateTimeFormatter对象，指定格式为："yyyy-MM-dd HH:mm"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        //调用format方法，将LocalDateTime对象转换为字符串
        orderInfo.setOdate(formatter.format(now));
        orderInfo.setStatus(status);
        orderInfo.setUid(uid);
        orderInfo.setPrice(price);
        orderInfo.setAid(aid);

        OrderInfo order = this.orderBiz.addOrderInfo(orderInfo);

        map.put("code", 1);
        map.put("data", order);

        return map;

    }

    //TODO  该方法目前是失效的
    //这个方法xzh压根就没写好  在前端没有找到调用接口

    /**
     * 下订方
     * 这个方法是结算界面点击结算按钮后就跳转支付平台
     * 正常来讲用户点击结算按钮后，进入此请求应该要判断当前商品是否能够进行结算  --即是否还有库存  如果没有库存则直接返回结算失败信息
     * 有就在此执行订单库存扣减（这一步正常来讲应该要在支付后的异步回调函数中执行，但支付回调接口无法稳定，就干脆写到这里算了）
     *
     * TODO 实现MQ异步通知
     * TODO 实现分布式锁防止超卖问题
     *
     * pid 手机id
     * num 订购数量
     * @return
     */
    @RequestMapping(value ="addOrderIteminfo" ,method = {RequestMethod.POST})
    @ApiOperation(value = "添加订单详情")
    public Map<String, Object> addOrderIteminfo( @RequestParam String pid,@RequestParam String num){

       return this.orderBiz.addOrderIteminfo(pid,num);

    }

    @RequestMapping(value ="findByOno" ,method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value = "查看订单")
    public Map<String, Object> findByOno(@RequestParam int ono){
        Map<String, Object> map=new HashMap<>();
        List<Map<String, List<OrderShowInfo>>> orderShowInfo = null;
        orderShowInfo = this.orderBiz.findByOno(ono);
        map.put("code", 1);
        map.put("data", orderShowInfo);
        return map;
    }

    @RequestMapping(value ="findAdd" ,method = {RequestMethod.POST})
    @ApiOperation(value = "查询地址")
    //tmd查询地址为什么不是findAddress
    public Map<String, Object> findAdd(@RequestParam int uid){
        Map<String, Object> map=new HashMap<>();
        List<Map<String, List<Address>>> add = null;
        add = this.orderBiz.findAdd(uid);
        map.put("code",1);
        map.put("data", add);
        return map;
    }


    /**
     * 删减商品数量方法
     * @param gnos
     * @param nums
     * @return
     */
    @RequestMapping(value ="updatePhoneinfo" ,method = {RequestMethod.POST})
    @ApiOperation(value = "修改商品数量")
    public Map<String, Object> updatePhoneinfo(@RequestParam int []gnos, @RequestParam int []nums){
        Map<String, Object> map=new HashMap<>();
        this.orderBiz.updatePhoneinfo(gnos, nums);
        map.put("code",1);
        return map;
    }



    @RequestMapping(value ="updateAdd" ,method = {RequestMethod.POST})
    @ApiOperation(value = "修改订单地址")
    public Map<String, Object> updateAdd(@RequestParam int ono, @RequestParam int aid){
        Map<String, Object> map=new HashMap<>();
        this.orderBiz.updateAdd(ono,aid);
        map.put("code",1);
        return map;
    }

}
