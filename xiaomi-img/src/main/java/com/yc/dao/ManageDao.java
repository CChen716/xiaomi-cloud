package com.yc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.bean.Manager;
import com.yc.bean.Phone;
import com.yc.bean.PhoneInfo;
import com.yc.beanVO.PhoneBrandVO;
import com.yc.beanVO.PhoneManageVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ManageDao extends BaseMapper {

    //后台管理系统查看所有
    //已进行sql优化1.0
    @Select("select  a.pid,num,price,pics,brand,name from tb_phoneinfo a inner join tb_phone b  on a.pid=b.pid group by pid")
    List<PhoneBrandVO> findAllManage();

    @Select(	"select  id,a.pid,color,num,price,pics,cap,brand,name from tb_phoneinfo a inner join tb_phone b  on a.pid=b.pid where b.pid=#{pid}")
    List<PhoneManageVO> findById(Integer pid);

    @Update("update tb_phoneinfo set num=#{num} where id=#{id}")
    int updateNum(Integer num,Integer id);

    @Options(useGeneratedKeys = true, keyProperty = "pid" , keyColumn ="pid")
    @Insert("insert into tb_phone values(null,#{name},#{brand})")
    int addPhone(Phone phone);


    @Insert("insert into tb_phoneinfo values(null,#{pid},#{color},#{num},#{price},#{pics},#{cap})")
    int addPhoneInfo(PhoneInfo phoneInfo);

    @Select("select id,username,pwd from manager ")
    List <Manager> findAdmin();




}
