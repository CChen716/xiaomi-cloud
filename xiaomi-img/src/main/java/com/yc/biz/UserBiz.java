package com.yc.biz;

import com.yc.bean.User;

import java.util.List;
import java.util.Map;

public interface UserBiz {
    public Map<String,Object> UserCountByRegDate();

    public List<User> findUser();

    public void deleteUser(Integer id);

    void testRPC(); //测试框架
}