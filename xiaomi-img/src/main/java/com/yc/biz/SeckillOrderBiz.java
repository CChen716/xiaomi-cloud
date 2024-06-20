package com.yc.biz;

import com.yc.bean.SeckillGoods;
import com.yc.bean.SeckillOrder;
import com.yc.bean.User;
import org.apache.http.nio.entity.NStringEntity;

import java.util.Map;

public interface SeckillOrderBiz {
    public SeckillOrder findSeckillOrderByuserid(Integer userid,Integer goodsid);
    public  void seckill(User user, SeckillGoods goods);
    public String getSeckillResult(Integer userid, Integer goodsid);
    public  SeckillOrder findDiscount(Integer  userid,Integer goodsid);





}