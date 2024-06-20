package com.yc.biz;

import com.yc.beanVO.PhoneBrandVO;
import com.yc.beanVO.SearchBottomVO;
import com.yc.entity.EsPhone;

import java.util.List;

public interface SearchBiz {
    //搜索页面根据搜索条件查询
    public List<EsPhone> findByBrand(String searchWord,Integer pageSize,Integer pageNo);
    //总数
    public int findTotal(String brand,String name);



}
