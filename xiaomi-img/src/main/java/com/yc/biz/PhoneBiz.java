package com.yc.biz;

import com.yc.beanVO.PhoneSlideVO;
import com.yc.beanVO.SearchTopVO;

import java.util.List;
import java.util.Map;

public interface PhoneBiz {
    //首页品牌栏部分
    public List<PhoneSlideVO> findBrandSlide();
    //首页顶部匹配框
    public List<SearchTopVO> findAll();

    //获取index.html中前八个元素根据brand品牌
    public Map<String,Object> getPhoneTop(String brand);

    //后台方法
    public Map<String,Integer> getBrandTotal();
    //后台方法
    public Map<String,Integer> getCountBrand();
}
