package com.yc.xiaomi.service;

import com.yc.bean.Address;
import com.yc.bean.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    public User reg(User user);
    public List<User> findUserByTel(String tel);
    public List<User> findUserByEmail(String email);
    public int changePwd(String email,String pwd);
    public int saveAddress(String address,String uid,String tel,String consignee);
    public List<Address> findAddressByUid(String uid,int start,int pagesize);
    public int updateUserInfo(User user);
    public List<Map<String,Object>> selectAllOrder(Integer id,int start,int pagesize);
    public List<Map<String,Object>> selectReceiveOrder(Integer id,int start,int pagesize);
    public List<Map<String,Object>> selectUnReceiveOrder(Integer id,int start,int pagesize);
    public List<Map<String,Object>> selectCloseOrder(Integer id,int start,int pagesize);
    public int updateStatus (String ono);
    public int refund(String ino);
    public int confirmReceive(String ono);
    public Map<String,Object> selectOnlyOrder(Integer id,Integer ino);
    public List<Map<String,Object>> selectByInoOrName(Integer id,String message,int start,int pagesize);
    public int deleteAddress(Integer id);
}
