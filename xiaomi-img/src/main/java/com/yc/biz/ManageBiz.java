package com.yc.biz;

import com.yc.bean.Manager;
import com.yc.bean.PhoneInfo;
import com.yc.beanVO.PhoneBrandVO;
import com.yc.beanVO.PhoneManageVO;

import java.util.List;

public interface ManageBiz {

    //后台管理系统查询所有商品
    public List<PhoneBrandVO> findAllManage();

    public List<PhoneManageVO> findById(Integer pid);

    public int updateNum(Integer num,Integer id);

    public Integer addPhone(String name,String brand);

    public void addPhoneInfo(String price, Integer num, String color, String version, String path, int pid);

    public List<Manager> findAdmin();



}
