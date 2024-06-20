package com.yc.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.bean.Com;
import com.yc.bean.Phone;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ComDao extends BaseMapper<Com> {
    @Select("select id,content,username,userid,foreignid,pid,target,createtime \n " +
            "from tb_com where \n"+
            "foreignid=#{pid} ")
    public List<Com> selectByForeignId(Integer pid);


    //详情页面
    @Select("select a.pid,name,brand,color,num,price,pics,cap from tb_phone a \n " +
            "left join tb_phoneinfo b \n" +
            "on a.pid=b.pid where a.pid=#{pid}\n")
    public List<Phone> selectPhoneByPid(Integer pid);


    //缓存穿透--布隆过滤器(添加商品id)
    @Select("select pid from tb_phone")
    public List<String> selectRedisBloomPhoneId();
}