package com.yc.xiaomi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.bean.Address;
import com.yc.bean.User;
import org.apache.ibatis.annotations.Select;

public interface UserDao extends BaseMapper<User> {
    @Select("select * from user where email=#{username} or tel=#{username} or username=#{username};")
    User selectUserByTelOREmail(String username);

}
