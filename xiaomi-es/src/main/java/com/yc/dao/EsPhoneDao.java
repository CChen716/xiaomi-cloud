package com.yc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.entity.EsPhone;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EsPhoneDao extends BaseMapper<EsPhone> {

    //将数据库中数据添加到ES中所用
    @Select("select  id,pid,name,brand,pics,price,num,color,cap from (select id,pid,name,pics,brand,price,num,color,cap from \n" +
            "            (select  id,a.pid,color,num,price,pics,cap,brand,name from tb_phoneinfo a inner join tb_phone b  on a.pid=b.pid ) \n" +
            "            A group by pid order by pid desc) B ")
    List<EsPhone> findAll();
}
