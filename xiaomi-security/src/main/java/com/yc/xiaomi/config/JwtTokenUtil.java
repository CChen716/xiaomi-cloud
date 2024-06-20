package com.yc.xiaomi.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yc.bean.User;
import com.yc.xiaomi.dao.UserDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//负责JWT操作,例如创建和验证
//token:Base64(头部)  Base64(载荷)   HS256(Base64(头部)+Base64(载荷),密钥)
@Component
public class JwtTokenUtil implements Serializable {
    //token的有效期
    public static final long JWT_TOKEN_VALIDITY=5*60*60;

    //注入密钥
    @Value("${jwt.secret}")
    private String secret="javainuse";

    @Autowired
    private UserDao userDao;


    //为用户生成token
    public String generateToken(UserDetails userDetails){
        //存载荷:七个默认值
        Map<String,Object> claims=new HashMap<>();
        //自己增加的载荷
        claims.put("username",userDetails.getUsername());
        claims.put("pwd",userDetails.getPassword());
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("username",userDetails.getUsername());
        wrapper.eq("pwd",userDetails.getPassword());
        User user=this.userDao.selectOne(wrapper);
        claims.put("id",user.getId());
        return doGenerateToken(claims,userDetails.getUsername());
    }
    //创建token步骤
    //1.定义令牌的声明,如Issuer,Expiration,Subject 和ID
    //2.使用HS512算法和密钥对JWT进行签名
    //3.根据JWT Compact Serialization 将JWT压缩为URL安全字符串
    private String doGenerateToken(Map<String,Object> claims,String subject){
        //jjwt依赖中提供的工具类:构造器模式
        //claim就是一个map
        return Jwts.builder()
                //有效载荷
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY*1000))
                //签名的运算
                .signWith(SignatureAlgorithm.HS512,secret)
                //压缩
                .compact();
    }

    //获取jwt token中的用户名
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token,Claims::getSubject);
    }

    //从jwt token中获取过期日期
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token,Claims::getExpiration);
    }
    //从token中获取claim
    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver){
        final Claims claims=getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //利用secret密钥从token中获取信息
    public Claims getAllClaimsFromToken(String token){
        //JWT依赖工具
        return Jwts
                .parser()  //构建解析器
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();//得到载荷
    }
    //检测token是否过期
    private Boolean isTokenExpired(String token){
        final Date expiration=getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //检测token
    public Boolean validateToken(String token,UserDetails userDetails){
        final String username=getUsernameFromToken(token);
        //用户名相同,且token不过期
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
