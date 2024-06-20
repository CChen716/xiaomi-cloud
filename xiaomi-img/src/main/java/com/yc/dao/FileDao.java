package com.yc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.bean.PhoneInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface FileDao extends BaseMapper<PhoneInfo> {

    @Select("select id,pics from tb_phoneinfo")
    List<PhoneInfo> findPics();


}
