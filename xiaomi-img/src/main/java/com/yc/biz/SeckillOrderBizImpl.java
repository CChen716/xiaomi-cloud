package com.yc.biz;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yc.bean.SeckillGoods;
import com.yc.bean.SeckillOrder;
import com.yc.bean.User;
import com.yc.dao.SeckillGoodsDao;
import com.yc.dao.SeckillOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Map;

@Service
public class SeckillOrderBizImpl implements SeckillOrderBiz {
    @Autowired
    private SeckillOrderDao seckillOrderDao;
    @Autowired
    private SeckillGoodsDao seckillGoodsDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public SeckillOrder findSeckillOrderByuserid(Integer userid, Integer goodsid) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("userid", userid);
        wrapper.eq("goodsid",goodsid);
        SeckillOrder seckillOrder = seckillOrderDao.selectOne(wrapper);
        return seckillOrder;
    }


    //秒杀业务
    //生成秒杀订单
    @Transactional
    @Override
    public void seckill(User user, SeckillGoods goods) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("goodsid",goods.getGoodsid());
        SeckillGoods seckillGoods = seckillGoodsDao.selectOne(wrapper);
        //将库存减一
        //TODO 此处应该改善    使用分布式锁和lua脚本
        if (seckillGoods.getStockcount()>0) {
            try {
                seckillGoods.setStockcount(seckillGoods.getStockcount() - 1);
                //生成订单 缺了一步
                seckillGoodsDao.updateById(seckillGoods);
                SeckillOrder seckillOrder = new SeckillOrder();
                seckillOrder.setGoodsid(goods.getGoodsid());
                seckillOrder.setUserid(user.getId());
                //生成秒杀订单
                seckillOrderDao.insert(seckillOrder);
                redisTemplate.opsForValue().set("seckillOrder:" + user.getId() + ":" + goods.getGoodsid(), seckillOrder);
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }else{
            //说明该商品已经买完了
            redisTemplate.opsForValue().set("goodsover"+seckillGoods.getGoodsid(),true);
        }
//这里返回应该未订单不是秒杀订单
    }

    @Override
    public String getSeckillResult(Integer userid, Integer goodsid) {
        SeckillOrder o = (SeckillOrder)redisTemplate.opsForValue().get("seckillOrder:" + userid + ":" + goodsid);
        if (o!=null){
            return "1";
        }else{
            //已经买完了所以goodsOver的返回值为零和-1
            boolean isOver=getGoodsOver(goodsid);
            if (isOver){
                return "-1";
            }else{
                return "0";
            }
        }
    }

    @Override
    public SeckillOrder findDiscount(Integer userid, Integer goodsid) {
        SeckillOrder discount = seckillOrderDao.findDiscount(userid, goodsid);
        return discount;
    }

    private boolean getGoodsOver( Integer goodsid) {
        return redisTemplate.hasKey("goodsover"+goodsid);
    }



}