package com.yc.xiaomi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.bean.Address;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AddressDao extends BaseMapper<Address> {
    @Select("select * from tb_address where uid=#{uid} limit #{start},#{pagesize} ")
    public List<Address> findAddressByUid(String uid,int start,int pagesize);
}
