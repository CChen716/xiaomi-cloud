package com.yc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.bean.Phone;
import com.yc.beanVO.PhoneBrandVO;
import com.yc.beanVO.PhoneSlideVO;
import com.yc.beanVO.SearchTopVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PhoneDao extends BaseMapper {

    //首页栏五个品牌的每个品牌各8个的方法
    //sql优化  v-1.0
    @Select("select pics,name,A.pid from (select name,pid,brand from tb_phone a where (select count(1) from tb_phone b where a.brand=b.brand and a.pid>=b.pid)<=8) A inner join (select pics,pid from tb_phoneinfo x " +
            "where (select pics,pid from tb_phoneinfo group by pid) B on A.pid=B.pid")
    List<PhoneSlideVO>  findByBrand();

    //查出所有手机的名字和ID 顶部搜索栏匹配使用
    //2024/3/26 已经过sql优化
    @Select("select brand,name from tb_phoneinfo a inner join tb_phone b  on a.pid=b.pid where a.num>0 group by a.pid")
    List<SearchTopVO> findAll();

    @Select("select A.pid,price,pics,name,brand,id,color,cap,num from (select name,pid,brand from tb_phone a where (select count(1) from tb_phone b where a.brand=b.brand and a.pid>=b.pid)<=8 and  a.brand = #{brand}) A inner join (select id,price, pics,pid,color,cap,num from tb_phoneinfo x where (select count(1) from tb_phoneinfo y  where x.pid=y.pid and x.id>=y.id)<=1 ) B on A.pid=B.pid\n" +
            "         order by A.pid")
    List<Phone> findTop(String brand);

    @Select("select distinct(brand) from tb_phone ")
    List<Phone> findBrand();

    @Select("select brand,SUM(num) as num from tb_phone a inner join tb_phoneinfo b on a.pid=b.pid group by brand")
    List<Phone> getCountBrand();

    @Select("select b.num, e.brand from (select a.pid,name,brand,color,num,price,id\n" +
            "from tb_phone a  inner join tb_phoneinfo c on a.pid=c.pid) e  inner join  (select ino,ono,gno,num from orderiteminfo where statu = 2 or statu =3) b on e.id=b.gno;")
    public List<Phone> getBrandTotal();




}
