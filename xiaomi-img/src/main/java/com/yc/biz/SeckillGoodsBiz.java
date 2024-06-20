package com.yc.biz;


import com.yc.bean.SeckillGoods;

import java.util.List;

public interface SeckillGoodsBiz {
    public SeckillGoods findSeckillGoodsByGoodsid(Integer goodsid);
    public List<SeckillGoods> findSeckillGoods();
    public SeckillGoods findSeckillByGoodId(Integer goodsid);

}