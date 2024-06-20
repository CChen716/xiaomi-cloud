package com.yc.xiaomi.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yc.bean.Address;
import com.yc.bean.User;
import com.yc.xiaomi.config.JwtTokenUtil;
import com.yc.xiaomi.dao.UserDao;
import com.yc.xiaomi.service.JwtUserDetailsService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@Slf4j
@RequestMapping("user")
@Api(tags = "用户登录鉴权管理")
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;  //spring security提供的验证管理器
    @Autowired
    private JwtTokenUtil jwtTokenUtil;        //生成和验证token的工具类
    @Autowired
    private JwtUserDetailsService userDetailsService;  //访问数据库,验证用户名和密码的业务类

    @Autowired
    private UserDao userDao;


    @RequestMapping({"/signout"})
    public Map<String,Object> signout(@RequestHeader("Authorization") String bearerToken,HttpSession session){
        Map<String,Object> result=new HashMap<>();
        session.removeAttribute("user");
        session.invalidate();
        result.put("code",1);
        return  result;
    }

    //根据id删除
    @RequestMapping(value = "deleteAddress",method = RequestMethod.GET)
    public Map<String,Object> deleteAddress(Integer id){
        Map<String,Object> map=new HashMap<>();
        int result=userDetailsService.deleteAddress(id);
        if (result==1){
            map.put("code",1);
        }else {
            map.put("code",0);
        }
        return map;
    }

    //通过商品名或订单号查询
    @RequestMapping(value = "selectByInoOrName",method = RequestMethod.GET)
    public Map<String,Object> selectByInoOrName(HttpSession session,String message, int pagenum, int pagesize){
        Map<String,Object> map=new HashMap<>();
        User user= (User) session.getAttribute("user");
        int start=(pagenum-1)*pagesize;
        List<Map<String,Object>> list=userDetailsService.selectByInoOrName(user.getId(),message,start,pagesize);
        List<Map<String,Object>> list2=userDetailsService.selectByInoOrName(user.getId(),message,0,100000);
        if (list.size()>0){
            int totalPage=list2.size()%3==0?list2.size()/3:list2.size()/3+1;
            map.put("order",list);
            map.put("code",1);
            map.put("totalPage",totalPage);
        }else {
            map.put("code",0);
        }
        return map;
    }

    //查询指定的订单
    @RequestMapping(value = "selectOnlyOrder",method = RequestMethod.GET)
    public Map<String,Object> selectOnlyOrder(HttpSession session,Integer ino){
        Map<String,Object> map=new HashMap<>();
        User user= (User) session.getAttribute("user");
        Map<String,Object> order=userDetailsService.selectOnlyOrder(user.getId(),ino);
        if (order.size()>0){
            map.put("order",order);
            map.put("code",1);
        }else {
            map.put("code",0);
        }
        return map;
    }

    //确认收货
    @RequestMapping(value = "confirmReceive",method = RequestMethod.POST)
    public Map<String,Object> confirmReceive(String ono){
        Map<String,Object> map=new HashMap<>();
        int result=userDetailsService.confirmReceive(ono);
        if (result>0){
            map.put("code",1);
        }else{
            map.put("code",0);
        }
        return map;
    }
    //退款
    @RequestMapping(value = "refund",method = RequestMethod.POST)
    public Map<String,Object> refund(String ino){
        Map<String,Object> map=new HashMap<>();
        int result=userDetailsService.refund(ino);
        if (result>0){
            map.put("code",1);
        }else{
            map.put("code",0);
        }
        return map;
    }

    //更新订单状态为已完成
    @RequestMapping(value = "updateStatus",method = RequestMethod.POST)
    public Map<String,Object> updateStatus(String ono){
        Map<String,Object> map=new HashMap<>();
        int result=userDetailsService.updateStatus(ono);
        if (result>0){
            map.put("code",1);
        }else{
            map.put("code",0);
        }
        return map;
    }

    //显示各种状态下的订单
    @RequestMapping(value = "showOrder",method = RequestMethod.GET)
    public Map<String,Object> showOrder(Integer id,int pagenum,int pagesize,int orderStatus){
        Map<String,Object> map=new HashMap<>();
        List<Map<String,Object>> order=new ArrayList<>();
        int start=(pagenum-1)*pagesize;
        //有效订单
        if (orderStatus==1) {
            order=userDetailsService.selectAllOrder(id,start,pagesize);
        }
        //未收货订单
        if (orderStatus==2){
            order=userDetailsService.selectUnReceiveOrder(id,start,pagesize);
        }
        //已收货订单
        if (orderStatus==3){
            order=userDetailsService.selectReceiveOrder(id,start,pagesize);
        }
        //已关闭订单
        if (orderStatus==4){
            order=userDetailsService.selectCloseOrder(id,start,pagesize);
        }
        if (order.size()>0){
            map.put("code",1);
            map.put("order",order);
        }else {
            map.put("code",0);
        }
        return map;
    }

    //更新个人信息
    @RequestMapping(value = "updateUser",method = RequestMethod.POST)
    public Map<String,Object> updateUser(User user){
        Map<String,Object> map=new HashMap<>();
        int result= userDetailsService.updateUserInfo(user);
        if (result!=1){
            map.put("code",0);
        }else {
            map.put("code",1);
        }

        return map;
    }




    //添加地址
    @RequestMapping(value = "saveAddress",method = RequestMethod.POST)
    public Map<String,Object> saveAddress(String address,String uid,String tel,String consignee){
        Map<String,Object> map=new HashMap<>();
        int result=userDetailsService.saveAddress(address,uid,tel,consignee);
        if (result==0){
            map.put("code",0);
            return map;
        }
        map.put("code",1);
        return map;

    }

    //检查电话
    @RequestMapping({"/checkTel"})
    public Map<String,Object> checkTel( String tel){
        Map<String,Object> result=new HashMap<>();
        List<User> list=userDetailsService.findUserByTel(tel);

        if (list.size()==0){
            result.put("code",1);
        }else {
            result.put("code",0);
        }
        return  result;
    }

    //检查邮箱
    @RequestMapping({"/checkEmail"})
    public Map<String,Object> checkEmail( String email){
        Map<String,Object> result=new HashMap<>();
        List<User> list=userDetailsService.findUserByEmail(email);
        if (list.size()==0){
            result.put("code",1);
        }else {
            result.put("code",0);
        }
        return  result;
    }

    //检查登录
    @RequestMapping({"/loginCheck"})
    public Map<String,Object> loginCheck(@RequestHeader("Authorization") String bearerToken,HttpSession session,int pagenum,int pagesize){
        System.out.println(  "\n"+session.getId() +"\n" );
        Map<String,Object> result=new HashMap<>();
        if (session.getAttribute("user")==null){
            result.put("code",0);
            result.put("msg","用户暂未登录");
            return result;
        }
        //再token认证
        System.out.println("接收到的token为:"+bearerToken);
        String token=bearerToken.substring(7);  //截去bearerToken中的"Bearer "
        User user=(User) session.getAttribute("user");
        //获取当前用户的id并查询此用户下的地址
        int start=(pagenum-1)*pagesize;
        List<Address> address=userDetailsService.findAddressByUid(user.getId().toString(),start,pagesize);
        //查询地址页面总页数
        int addressTotalpages=0;
        List<Address> address2=userDetailsService.findAddressByUid(user.getId().toString(),0,100000);
        addressTotalpages=address2.size()%2==0?address2.size()/2:address2.size()/2+1;
        //查询订单页面的总页数
        Integer id=user.getId();
        //查询有效订单条数
        List<Map<String,Object>> list1= userDetailsService.selectAllOrder(id,0,100000);
        if (list1.size()>0){
            int page1=list1.size()%3==0?list1.size()/3:list1.size()/3+1;
            result.put("totalPage1",page1);
            result.put("size1",list1.size());
        }
        //查询未收货订单条数
        List<Map<String,Object>> list2=userDetailsService.selectUnReceiveOrder(id,0,100000);
        if (list2.size()>0){
            int page2=list2.size()%3==0?list2.size()/3:list2.size()/3+1 ;
            result.put("totalPage2",page2);
            result.put("size2",list2.size());
        }
        //查询已收货订单条数
        List<Map<String,Object>> list3=userDetailsService.selectReceiveOrder(id,0,100000);
        if (list3.size()>0){
            int page3=list3.size()%3==0?list3.size()/3:list3.size()/3+1 ;
            result.put("totalPage3",page3);
        }
        //查询已关闭订单条数
        List<Map<String,Object>> list4=userDetailsService.selectCloseOrder(id,0,100000);
        if (list4.size()>0){
            int page4=list4.size()%3==0?list4.size()/3:list4.size()/3+1 ;
            result.put("totalPage4",page4);
        }

        //初始化第一页有效订单
        List<Map<String,Object>> list5=userDetailsService.selectAllOrder(id,0,3);
        if (list5.size()>0){
            result.put("usefulOrder",list5);
        }

        UserDetails ud=userDetailsService.loadUserByUsername2(user.getUsername());
        boolean b=jwtTokenUtil.validateToken(token,ud);   //校验用户名与有效时间
        if (b){
            result.put("code",1);
            result.put("data",user);
            result.put("address",address);
            result.put("addressTotalpages",addressTotalpages);
        }else {
            //无效
            result.put("code",0);
        }
        return result;
    }


    @RequestMapping({"/getTokenDetail"})
    public Claims firstPage(@RequestHeader("Authorization") String bearerToken){
        System.out.println("接收到的  token为:"+bearerToken);
        String token=bearerToken.substring(7);   //bearerToken 它的开头是"bearer ",要删除
        Claims c=jwtTokenUtil.getAllClaimsFromToken(token);
        return c;
    }

    //更改密码
    @RequestMapping(value = "changePwd",method = RequestMethod.POST)
    public Map<String,Object> changePwd( String email,String pwd){
        Map<String,Object> result=new HashMap<>();
        List<User> list=userDetailsService.findUserByEmail(email);
        BCryptPasswordEncoder be=new BCryptPasswordEncoder();
        pwd=be.encode(pwd);
        if (list.size()==0){
            //在用户表中查找不到此邮箱
            result.put("code",0);
        }else {
            //更新密码
            int data=userDetailsService.changePwd(email,pwd);
            if (data==1){
                result.put("code",1);
            }
        }
        return result;
    }


    @ApiOperation(value = "用户登录操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name="username",value = "登录账号",required = true),
            @ApiImplicitParam(name="upass",value = "密码",required = true),
            @ApiImplicitParam(name="valcode",value = "验证码",required = true)
    })
    @RequestMapping(value = "/login.action",method = RequestMethod.POST)
    public Map<String ,Object> createAuthenticationToken(String username, String pwd,String code, HttpSession session) throws Exception {
        Map<String,Object> result=new HashMap<>();
        String finalCode=(String)session.getAttribute("code");
        if (!finalCode.equals(code)){
            result.put("code",0);
            result.put("msg","验证码错误");
            return result;
        }
        authenticate(username,pwd);
        //根据用户名取出详情
        final UserDetails userDetails=userDetailsService.loadUserByUsername(username);
        //根据用户详情生成token
        final String token=jwtTokenUtil.generateToken(userDetails);
        //在业务层多增加一个方法,根据uname再查一次User(com.yc.bean)
        User user=userDao.selectUserByTelOREmail(username);

        session.setAttribute("user",user);   //存到session中
        //将 token回传会客户端
        result.put("code",1);
        result.put("data",token);
        result.put("user",user);
        return result;
    }

    @ApiOperation(value = "用户注册操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name="uname",value = "用户名",required = true),
            @ApiImplicitParam(name="upass",value = "密码",required = true),
            @ApiImplicitParam(name="head",value = "头像",required = true),
            @ApiImplicitParam(name="gender",value = "性别",required = true),
            @ApiImplicitParam(name="code",value = "验证码",required = true)
    })
    @RequestMapping(value = "/reg.action",method = RequestMethod.POST)
    public Map<String,Object> reg(User user,HttpSession session){
        Map<String,Object> result=new HashMap<>();
        Date d=new Date();
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        user.setRegDate(df.format(d));
        //密码在数据库中加密
        BCryptPasswordEncoder be=new BCryptPasswordEncoder();
        user.setPwd(be.encode(user.getPwd()));
        User u=this.userDetailsService.reg(user);
        result.put("code",1);
        result.put("data",u);
        return result;
    }

    private void authenticate(String username,String password) throws Exception {
        try {
            //调用认证管理器  对输入的用户名和密码进行认证

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        }catch (DisabledException e){
            throw new Exception("USER_DISABLED",e);
        }catch (BadCredentialsException e){
            throw new Exception("INVALID_CREDENTIALS",e);
        }
    }
}
