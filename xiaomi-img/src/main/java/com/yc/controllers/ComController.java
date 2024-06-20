package com.yc.controllers;

import com.yc.bean.Com;
import com.yc.bean.Phone;
import com.yc.biz.ComBiz;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.datatransfer.Clipboard;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("com")
public class ComController {
    @Autowired
    private ComBiz comBiz;

    @GetMapping("findComByForeignid")
    public Map<String,Object> findComByForeignid(@RequestParam Integer pid){
        Map<String,Object> map=new HashMap<>();
        Map<Integer, List<Com>> mapList=null;
        try {
            mapList=this.comBiz.findComByForeignId(pid);
        }catch (Exception e){
            e.printStackTrace();
            map.put("code",0);
            map.put("msg",e.getCause());
            return map;
        }
        map.put("code",1);
        map.put("data",mapList);
        return map;
    }
    //添加评论
    @PostMapping("addCom")
    public Map<String,Object> addCom(Com com){
        Map<String,Object> map=new HashMap<>();
        Integer status=this.comBiz.addCom(com);
        if (status<=0){
            map.put("code",0);
            map.put("msg","添加失败");
            return map;

        }
        map.put("code",1);
        return map;
    }



    //详情页面请求方法
    @RequestMapping(value = "findPhoneByPid",method={RequestMethod.GET})
    public Map<String,Object> findPhoneByPid(@RequestParam Integer pid){
        Map<String,Object> map =new HashMap<>();
        List<Phone> t=this.comBiz.findPhoneByPid(pid);
        map.put("code",1);
        map.put("data",t);
        return map;
    }




}
