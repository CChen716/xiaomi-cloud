package com.yc.controllers;

import com.yc.beanVO.PhoneSlideVO;
import com.yc.beanVO.SearchTopVO;
import com.yc.biz.PhoneBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页提供图片的控制层
 */
@RestController
@RequestMapping("index")
@Slf4j
@Api(tags = "首页图片")  //swagger框架提供
public class IndexController {
    @Autowired
    private PhoneBiz phoneBiz;

    @GetMapping("findSlide")
    @ApiOperation(value = "查询首页品牌栏")
    public Map<String,Object> findSlidePhone(){
        Map<String,Object> map=new HashMap<>();
        List<PhoneSlideVO> list=null;
        try {
            list=this.phoneBiz.findBrandSlide();
        }catch (Exception e){
            map.put("code",0);
            map.put("msg",e.getCause());
            e.printStackTrace();
            return map;
        }
        map.put("code",1);
        map.put("SlidePhone",list);
        return map;
    }

    @GetMapping("findAll")
    @ApiOperation(value = "搜索框字段匹配需要")
    public Map<String,Object> findAllPhone(){
        Map<String,Object> map=new HashMap<>();
        List<SearchTopVO> list=null;
        try {
            list=this.phoneBiz.findAll();
        }catch (Exception e){
            map.put("code",0);
            map.put("msg",e.getCause());
            e.printStackTrace();
            return map;
        }
        map.put("code",1);
        map.put("phoneAll",list);
        return map;
    }

    @RequestMapping(value = "getPhoneTop",method = {RequestMethod.POST})
    public List<Map<String,Object>> getPhoneTop(@RequestParam String[] brand){
        List<Map<String,Object>> list=new ArrayList<>();
        for (String item:brand){
            Map<String, Object> phoneTop = this.phoneBiz.getPhoneTop(item);
            list.add(phoneTop);

        }
        return list;



    }
    @RequestMapping(value = "getBrandTotal",method={RequestMethod.GET})
    public Map<String,Integer> getBrandTotal(){
        return this.phoneBiz.getBrandTotal();
    }
    @RequestMapping(value ="getCountBrand",method={RequestMethod.GET} )
    public Map<String, Integer> getCountBrand() {
        return this.phoneBiz.getCountBrand();


    }

}
