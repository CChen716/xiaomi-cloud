package com.yc.controllers;

import com.yc.beanVO.PhoneBrandVO;
import com.yc.beanVO.SearchBottomVO;
import com.yc.beanVO.SearchTopVO;
import com.yc.biz.SearchBiz;
import com.yc.util.CacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("search")
@Slf4j
@Api(tags = "搜索页面图片")  //swagger框架提供
public class SearchController {

    @Autowired
    private CacheService cacheService;  //redis封装类


    @Autowired
    private SearchBiz searchBiz;

    @RequestMapping(value = "findPage",method = {RequestMethod.GET})
    @ApiOperation(value = "搜索页面查询操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "brand",value = "品牌",readOnly = true),
            @ApiImplicitParam(name = "name",value = "型号",readOnly = true),
    })
    public Map<String,Object> findByBrand(@RequestParam String brand,@RequestParam String name){
        Map<String,Object> map=new HashMap<>();
        List<PhoneBrandVO> list=null;
        int total;
        try {
            list=searchBiz.findByBrand(brand,name);
            total=searchBiz.findTotal(brand,name);
        }catch (Exception e){
            map.put("code",0);
            map.put("msg",e.getCause());
            e.printStackTrace();
            return map;
        }
        map.put("code",1);
        map.put("total",total);
        map.put("phones",list);
        return map;
    }



    @GetMapping("findBottom")
    @ApiOperation(value = "搜索页面底部推荐")
    public Map<String,Object> findBottom(){
        Map<String,Object> map=new HashMap<>();
        List<SearchBottomVO> list=null;
        try {
            list=this.searchBiz.findBottom();
        }catch (Exception e){
            map.put("code",0);
            map.put("msg",e.getCause());
            e.printStackTrace();
            return map;
        }
        map.put("code",1);
        map.put("phonesBottom",list);
        return map;
    }
}
