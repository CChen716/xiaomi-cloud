package com.yc.biz;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rpc.annotation.RpcReference;
import com.yc.bean.User;
import com.yc.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS,
        isolation = Isolation.DEFAULT,timeout = 2000,
        readOnly = true,rollbackFor = RuntimeException.class)
public class UserBizImpl implements UserBiz {

    @Autowired
    private UserDao userDao;

    @RpcReference //测试rpc
    private CartBiz cartBiz;


    @Override
    public Map<String,Object> UserCountByRegDate() {
        Map<String,Object> result=new HashMap<>();
        Date today=new Date();
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd");
        //今日新增的数量
        int i = userDao.userCountByRegDate(spf.format(today));
        QueryWrapper wrapper=new QueryWrapper();
        Integer totalUserCount = userDao.selectCount(wrapper);
        result.put("newUser",i);
        result.put("totalCount",totalUserCount);


        return result;
    }

    @Override
    public List<User> findUser() {
       return this.userDao.findUser();
    }

    //根据用户id删除用户
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT,timeout = 2000,
            rollbackFor = RuntimeException.class)
    @Override
    public void deleteUser(Integer id) {
        this.userDao.deleteById(id);
    }


    //rpc测试 开放接口注解
    public void testRPC(){
       System.out.println(cartBiz.TestRpc("66", "8"));
    }


}