package com.yc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.yc.bean.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserDao extends BaseMapper<User> {
    @Select("SELECT count(1) " +
            "FROM `user` " +
            "WHERE   DATE(regDate) = #{date}")
    public int userCountByRegDate(String date);

    @Select("select id,username,tel,pwd,regDate,email from user")
    List<User> findUser();

}