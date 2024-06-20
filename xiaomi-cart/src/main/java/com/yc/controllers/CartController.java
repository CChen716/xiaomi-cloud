package com.yc.controllers;

import com.yc.bean.CartInfo;
import com.yc.bean.User;
import com.yc.beanVO.CartShowInfo;
import com.yc.biz.CartBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("cart")
@Slf4j
@Api(tags = "购物车")
public class CartController {

    @Autowired
    private CartBiz cartBiz;

    @RequestMapping(value = "findByUno", method = {RequestMethod.GET})
    @ApiOperation(value = "根据Uno查询购物车")
    public Map<String, Object> findByUno(@RequestParam Integer uno){
        Map<String, Object> map = new HashMap<>();
        List<Map<String, List<CartShowInfo>>> cartShowInfo = null;
        cartShowInfo = this.cartBiz.findByUno(uno);
        map.put("code", 1);
        map.put("data", cartShowInfo);
        return map;
    }

    @RequestMapping(value = "del",method = {RequestMethod.POST})
    @ApiOperation(value = "删除商品")
    public Map<String,Object> del(@RequestParam String cno){
        Map<String,Object> map=new HashMap<>();
        this.cartBiz.del(cno);
        map.put("code",1);
        return map;
    }

    @RequestMapping(value = "dels",method = {RequestMethod.POST})
    @ApiOperation(value = "删除多个商品")
    public Map<String,Object> dels(@RequestParam int[] cnos,@RequestParam Integer ono){
        Map<String,Object> map=new HashMap<>();
        this.cartBiz.dels(cnos,ono);
        map.put("code",1);
        return map;
    }

    @RequestMapping(value ="add" ,method = {RequestMethod.POST})
    @ApiOperation(value = "添加购物车")
    public Map<String,Object> add(CartInfo cart,HttpServletRequest req){
        Map<String,Object> map=new HashMap<>();
       // 从Session中获取用户Id
        User user = (User) req.getSession().getAttribute("user");
        cart.setUno(user.getId());
        this.cartBiz.add(cart);
        map.put("code",1);
        return map;
    }

    @RequestMapping(value ="totalCartNum" ,method = {RequestMethod.POST})
    @ApiOperation(value = "统计某个会员的购物车中商品个数")
    public int totalCartNum(int uno){
        int cart = this.cartBiz.totalCartNum(uno);
        return cart;
    }

    @RequestMapping(value ="update" ,method = {RequestMethod.POST})
    @ApiOperation(value = "增加商品数量")
    public int update(@RequestParam int num,@RequestParam int cno){
        this.cartBiz.update(num,cno);
        return cno;
    }
}
