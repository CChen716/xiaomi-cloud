package com.yc.xiaomi.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yc.bean.Address;
import com.yc.bean.OrderInfo;
import com.yc.bean.OrderItemInfo;
import com.yc.bean.User;
import com.yc.xiaomi.dao.AddressDao;
import com.yc.xiaomi.dao.OrderDao;
import com.yc.xiaomi.dao.OrderItemDao;
import com.yc.xiaomi.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService,UserService {
    @Autowired
    private UserDao userdao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userdao.selectUserByTelOREmail(username);
        if (user!=null){
            //spring security的User类                                权限
            return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPwd(),new ArrayList<>());
        }else {
            throw new UsernameNotFoundException("User not found with username:"+username);
        }
    }

    public UserDetails loadUserByUsername2(String username) throws UsernameNotFoundException {
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("username",username);
        User user=userdao.selectOne(wrapper);
        if (user!=null){
            //spring security的User类                                权限
            return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPwd(),new ArrayList<>());
        }else {
            throw new UsernameNotFoundException("User not found with username:"+username);
        }
    }

    //根据aid删除
    @Override
    public int deleteAddress(Integer id) {
        int result=addressDao.deleteById(id);
        return result;
    }

    //根据商品名或订单号查询
    @Override
    public List<Map<String, Object>> selectByInoOrName(Integer id, String message, int start, int pagesize) {
        List<Map<String,Object>> list=  orderDao.selectByInoOrName(id,message,start,pagesize);
        return list;
    }

    //查询选定编号的订单
    @Override
    public Map<String, Object> selectOnlyOrder(Integer id, Integer ino) {
        Map<String,Object> map=orderDao.selectOnlyOrder(id,ino);
        return map;
    }

    //显示订单信息
    @Override
    public List<Map<String,Object>> selectAllOrder(Integer id,int start,int pagesize){
        List<Map<String,Object>> list=orderDao.selectAllOrder(id,start,pagesize);
        return list;
    }

    //查询已收货订单
    @Override
    public List<Map<String, Object>> selectReceiveOrder(Integer id, int start, int pagesize) {
        List<Map<String,Object>> list=orderDao.selectReceiveOrder(id,start,pagesize);
        return list;
    }

    //查询未收货订单
    @Override
    public List<Map<String, Object>> selectUnReceiveOrder(Integer id, int start, int pagesize) {
        List<Map<String,Object>> list=orderDao.selectUnReceiveOrder(id,start,pagesize);
        return list;
    }

    //查询关闭订单
    @Override
    public List<Map<String, Object>> selectCloseOrder(Integer id, int start, int pagesize) {
        List<Map<String,Object>> list=orderDao.selectCloseOrder(id,start,pagesize);
        return list;
    }

    //更新用户信息
    @Override
    public int updateUserInfo(User user) {
        UpdateWrapper<User> updateWrapper=new UpdateWrapper<>();
        if (!user.getUsername().isEmpty()){
            updateWrapper.eq("id",user.getId()).set("username",user.getUsername());
        }
        if (!user.getEmail().isEmpty()){
            updateWrapper.eq("id",user.getId()).set("email",user.getEmail());
        }
        if (!user.getTel().isEmpty()){
            updateWrapper.eq("id",user.getId()).set("tel",user.getTel());
        }
        if (!user.getPwd().isEmpty()){
            BCryptPasswordEncoder be=new BCryptPasswordEncoder();
            user.setPwd(be.encode(user.getPwd()));
            updateWrapper.eq("id",user.getId()).set("pwd",user.getPwd());
        }
       return userdao.update(null,updateWrapper);
    }

    //确认收货
    @Override
    public int confirmReceive(String ono) {
        UpdateWrapper<OrderInfo> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("ono",ono).set("status","3");
        return  orderDao.update(null,updateWrapper);
    }

    //退款
    @Override
    public int refund(String ino) {
        UpdateWrapper<OrderItemInfo> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("ino",ino).set("statu",0);
        return orderItemDao.update(null,updateWrapper);
    }

    //更新订单状态为已完成
    @Override
    public int updateStatus(String ono) {
        UpdateWrapper<OrderInfo> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("ono",ono).set("status","1");
        return  orderDao.update(null,updateWrapper);
    }

    //根据uid查询地址
    @Override
    public List<Address> findAddressByUid(String uid,int start,int pagesize) {
       QueryWrapper<Address> wrapper=new QueryWrapper<>();
       wrapper.eq("uid",uid);
       List<Address> address=addressDao.findAddressByUid(uid,start,pagesize);
       return address;
    }

    //添加地址
    @Override
    public int saveAddress(String address, String uid, String tel, String consignee) {
        Address add=new Address();
        add.setAddress(address);
        add.setUid(Integer.parseInt(uid));
        add.setTel(tel);
        add.setConsignee(consignee);
        addressDao.insert(add);
        return 1;
    }

    @Override
    public User reg(User user) {
        userdao.insert(user);
        return user;
    }

//    //根据电话或姓名查询用户
//    @Override
//    public User findUserByUname(String username) {
//        QueryWrapper<User> wrapper=new QueryWrapper<>();
//        wrapper.eq("username",username);
//        User user=userdao.selectOne(wrapper);
//        return user;
//    }

    //根据电话查询用户
    @Override
    public List<User> findUserByTel(String tel) {
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("tel",tel);
        List<User> list=userdao.selectList(wrapper);
        return list;
    }

    //根据邮箱查询用户
    @Override
    public List<User> findUserByEmail(String email) {
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("email",email);
        List<User> list=userdao.selectList(wrapper);

        return list;
    }

    //更改密码
    @Override
    public int changePwd(String email,String pwd) {
        UpdateWrapper<User> updateWrapper= Wrappers.update();
        updateWrapper.eq("email",email);
        User user=new User();
        user.setPwd(pwd);
         int result= userdao.update(user,updateWrapper);
         return result;
    }



}