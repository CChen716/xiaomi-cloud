package com.yc.controllers;


import com.yc.bean.Manager;
import com.yc.bean.PhoneInfo;
import com.yc.beanVO.PhoneBrandVO;
import com.yc.beanVO.PhoneManageVO;
import com.yc.biz.ManageBizImpl;
import com.yc.util.FdfsClientWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("Manage")
@Slf4j
@Api(tags = "后台系统")  //swagger框架提供
public class ManageController {

    @Autowired
    private ManageBizImpl manageBiz;

    //文件上传功能
    @Autowired
    private FdfsClientWrapper fdfsClientWrapper;


    @GetMapping("findAdmin")
    @ApiOperation(value = "管理员登录")
    public Map<String,Object> findAdmin(){
        Map<String,Object> map=new HashMap<>();
        List<Manager> list=null;
        try {
            list=this.manageBiz.findAdmin();
        }catch (Exception e){
            map.put("code",0);
            map.put("msg",e.getCause());
            e.printStackTrace();
            return map;
        }
        map.put("code",1);
        map.put("manager",list);
        return map;
    }

    @GetMapping("findAllManage")
    @ApiOperation(value = "后台管理查询所有商品")
    public Map<String,Object> findAllManage(){
        Map<String,Object> map=new HashMap<>();
        List<PhoneBrandVO> list=null;
        int total;
        try {
            list=this.manageBiz.findAllManage();
            total=list.size();
        }catch (Exception e){
            map.put("code",0);
            map.put("msg",e.getCause());
            e.printStackTrace();
            return map;
        }
        map.put("code",1);
        map.put("phones",list);
        map.put("total",total);
        return map;
    }

    @RequestMapping(value = "findByPid",method = {RequestMethod.GET})
    @ApiOperation(value = "后台管理系统详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid",value = "编号",readOnly = true),
    })
    public Map<String,Object> findById( @RequestParam Integer pid){
        Map<String,Object> map=new HashMap<>();
        List<PhoneManageVO> list=null;
        try {
            list=this.manageBiz.findById(pid);
        }catch (Exception e){
            map.put("code",0);
            map.put("msg",e.getCause());
            e.printStackTrace();
            return map;
        }
        map.put("code",1);
        map.put("phones",list);
        return map;
    }


      //修改商品业务   配合Redis  当商品发生修改时候  应该清除其Redis中的数据
    @RequestMapping(value = "updateNum",method = {RequestMethod.POST})
    @ApiOperation(value = "根据id修改库存数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "编号",readOnly = true),
            @ApiImplicitParam(name = "num",value = "库存数",readOnly = true),
    })
    public Map<String,Object> updateNum( @RequestParam Integer id,@RequestParam Integer num){
        Map<String,Object> map=new HashMap<>();
        try {
            this.manageBiz.updateNum(num,id);
        }catch (Exception e){
            map.put("code",0);
            map.put("msg",e.getCause());
            e.printStackTrace();
            return map;
        }
        map.put("code",1);
        return map;
    }

    //添加新商品
    @RequestMapping(value = "addPhone",method = {RequestMethod.POST})
    @ApiOperation(value = "根据id修改库存数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "手机名",readOnly = true),
            @ApiImplicitParam(name = "brand",value = "品牌名",readOnly = true),
            @ApiImplicitParam(name = "price",value = "价格",readOnly = true),
            @ApiImplicitParam(name = "num",value = "库存数",readOnly = true),
            @ApiImplicitParam(name = "color",value = "颜色",readOnly = true),
            @ApiImplicitParam(name = "version",value = "内存版本",readOnly = true),
    })
    public Map<String,Object> addPhone( @RequestParam String name,
                                        @RequestParam String brand,
                                        @RequestParam String price,
                                        @RequestParam Integer num,
                                        @RequestParam String color,
                                        @RequestParam String version,
                                        @RequestParam String path)  //此处通过前端调用上传方法 异步返回请求上传成功的图片路径结果传入
    {
        Map<String,Object> map=new HashMap<>();
        try { //需要更新两张表  其次是文件上传

          Integer pid=this.manageBiz.addPhone(name,brand);   //先添加到phone表中  此处需要添加的主键作为info表的参数 返回pid主键 //TODO 判断非空
          //拿到pid 与图片路径path后  作为info表的参数
          this.manageBiz.addPhoneInfo(price,num,color,version,path,pid); //将数据添加到info表中
        }catch (Exception e){
            map.put("code",0);
            map.put("msg",e.getCause());
            e.printStackTrace();
            return map;
        }
        map.put("code",1);
        return map;
    }



}
