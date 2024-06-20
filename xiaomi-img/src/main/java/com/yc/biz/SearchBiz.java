package com.yc.biz;

import com.yc.beanVO.PhoneBrandVO;
import com.yc.beanVO.SearchBottomVO;

import java.util.List;

public interface SearchBiz {
    //搜索页面根据搜索条件查询
    public List<PhoneBrandVO> findByBrand(String brand,String name);
    //总数
    public int findTotal(String brand,String name);
    //搜索页面底部推荐
    public List<SearchBottomVO> findBottom();


}
