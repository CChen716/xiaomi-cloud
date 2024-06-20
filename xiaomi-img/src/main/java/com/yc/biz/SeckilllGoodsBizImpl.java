package com.yc.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yc.bean.SeckillGoods;
import com.yc.dao.SeckillGoodsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeckilllGoodsBizImpl implements SeckillGoodsBiz {
    @Autowired
    private SeckillGoodsDao seckillGoodsDao;
    public SeckillGoods findSeckillGoodsByGoodsid(Integer goodsid){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("goodsid",goodsid);
        SeckillGoods seckillGoods=seckillGoodsDao.selectOne(wrapper);
        return seckillGoods;
    }
    @Override
    public List<SeckillGoods> findSeckillGoods(){
        QueryWrapper wrapper=new QueryWrapper();
        List<SeckillGoods> list = seckillGoodsDao.selectList(wrapper);
        return list;
    }

    @Override
    public SeckillGoods findSeckillByGoodId(Integer goodsid) {
        QueryWrapper wrapper= new QueryWrapper();
        wrapper.eq("goodsid",goodsid);
        SeckillGoods seckillGoods = seckillGoodsDao.selectOne(wrapper);
        return seckillGoods;
    }
}
