package com.yc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.beanVO.PhoneBrandVO;
import com.yc.beanVO.SearchBottomVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SearchDao extends BaseMapper {


    //分页查询的总商品条数   //TODO 需要测试
    @Select(" select count(*) from (select pid,name,pics,brand,price,num from " +
            "                (select  id,a.pid,color,num,price,pics,cap,brand,name from tb_phoneinfo a inner join tb_phone b  on a.pid=b.pid) A  " +
            "                group by pid) B where brand like #{brand} and name like #{name} ")
    int totalPage(String brand,String name);

    //分页查询  根据品牌名进行查询  根据传入的品牌名和手机型号名(前端进行处理好后再传回)--》以前是通过后端进行处理传入的字符串
    @Select("select pid,name,pics,brand,price,num from (select pid,name,pics,brand,price,num from " +
            "(select  id,a.pid,color,num,price,pics,cap,brand,name from tb_phoneinfo a inner join tb_phone b  on a.pid=b.pid where a.num>0) A " +
            "group by pid) B  where brand like #{brand} and name like #{name} ")
    List<PhoneBrandVO> findByBrand(String brand,String name);

    //搜索页面底部查询
    @Select("select pics,name,price,A.pid from (select name,pid,brand from tb_phone a where (select count(1) from tb_phone b where a.brand=b.brand and a.pid>=b.pid)<=1) A " +
            "inner join (select pics,pid,price from tb_phoneinfo x where (select count(1) from tb_phoneinfo y  where x.pid=y.pid and x.id>=y.id)<=1 ) B on A.pid=B.pid")
    List<SearchBottomVO> findBottom();





}
