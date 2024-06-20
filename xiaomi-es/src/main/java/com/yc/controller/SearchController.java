package com.yc.controller;

import com.yc.beanVO.PhoneBrandVO;

import com.yc.biz.EsService;
import com.yc.biz.SearchBiz;
import com.yc.entity.EsPhone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("search")
@Slf4j
public class SearchController {

    @Autowired
    private SearchBiz searchBiz;


    @RequestMapping(value = "findPage",method = {RequestMethod.GET})
    public Map<String,Object> findByBrand(@RequestParam String searchWord,@RequestParam Integer pageSize,@RequestParam Integer pageNo){
        Map<String,Object> map=new HashMap<>();
        List<EsPhone> list=null;
        int total;
        try {
          list=searchBiz.findByBrand(searchWord,pageSize,pageNo);
        }catch (Exception e){
            map.put("code",0);
            map.put("msg",e.getCause());
            e.printStackTrace();
            return map;
        }
        map.put("code",1);
    //    map.put("total",total);
        map.put("phones",list);
        return map;
    }




}
