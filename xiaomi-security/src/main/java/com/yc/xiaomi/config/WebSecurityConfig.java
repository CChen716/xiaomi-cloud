package com.yc.xiaomi.config;

import com.yc.xiaomi.controller.JwtAuthenticationEntryPoint;
import com.yc.xiaomi.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;  //出错了的处理类
    @Autowired
    private UserDetailsService jwtUserDetailsService;    //业务类
    @Autowired
    private JwtRequestFilter jwtRequestFilter;          //过滤器

    //@Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();      //加密算法类
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //关闭csrf跨站请求伪造
        httpSecurity.csrf().disable()
                //不验证此特定请求

                .authorizeHttpRequests().antMatchers("/user/login.action","/user/loginCheck","/user/selectOnlyOrder","/user/reg.action","/user/checkTel","/user/changePwd","/user/checkEmail","/email/sendEmail","/user/code.action","v3/api-docs","/swagger-resources/**","/swagger-ui/**","/swagger-ui.html").permitAll().
                //所有其他请求都需要经过身份验证
                        anyRequest().authenticated().
                and().
                //验证出错则会401
                        exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().
                sessionManagement()
                //确保我们使用无状态会话:会话不会用于存储用户的状态
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //添加一个筛选器已验证每个请求的令牌
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
