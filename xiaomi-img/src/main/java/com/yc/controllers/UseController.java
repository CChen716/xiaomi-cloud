package com.yc.controllers;

import com.yc.bean.User;
import com.yc.beanVO.PhoneBrandVO;
import com.yc.beanVO.SearchBottomVO;
import com.yc.biz.UserBiz;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UseController {
    @Autowired
    private UserBiz userBiz;

    @RequestMapping(value = "findUserCount",method = {RequestMethod.GET})
    public Map<String,Object> findUserCount(){
        Map<String, Object> map = userBiz.UserCountByRegDate();
        return map;
    }

    @GetMapping("findUser")
    @ApiOperation(value = "用户搜索")
    public Map<String,Object> findBottom(){
        Map<String,Object> map=new HashMap<>();
        List<User> list=null;
        try {
            list=this.userBiz.findUser();
        }catch (Exception e){
            map.put("code",1);
            map.put("msg",e.getCause());
            e.printStackTrace();
            return map;
        }
        map.put("code",0); //此处是layui后台表格数据正常是 0
        map.put("data",list);
        return map;
    }


    //删除用户

    @RequestMapping(value = "deleteUser",method = {RequestMethod.POST})
    @ApiOperation(value = "搜索页面查询操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",readOnly = true)
    })
    public Map<String,Object> deleteUser(@RequestParam Integer id){
        Map<String,Object> map=new HashMap<>();
        try {
            this.userBiz.deleteUser(id);
        }catch (Exception e){
            map.put("code",0);
            map.put("msg",e.getCause());
            e.printStackTrace();
            return map;
        }
        map.put("code",1);
        return map;
    }

    @RequestMapping(value = "testRpc",method = {RequestMethod.GET})
    public void testRpc(){
        userBiz.testRPC();
    }

}