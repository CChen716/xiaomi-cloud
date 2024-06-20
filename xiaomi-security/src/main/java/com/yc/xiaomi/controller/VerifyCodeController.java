package com.yc.xiaomi.controller;


import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

@RestController
@Slf4j
@RequestMapping("user")
@Api(tags = "验证码获取")
public class VerifyCodeController {
    @GetMapping("/code.action")
    public HttpEntity image(HttpSession session) throws IOException {  //
        //创建对象,存图片(验证码图片对象)
        int width=100;
        int height=50;
        BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
        //2.美化图片
        //2.1填充背景色
        Graphics graphics=image.getGraphics(); //画笔对象
        graphics.setColor(Color.PINK);//画笔的颜色
        graphics.fillRect(0,0,width,height);//fillRect方法可以画背景颜色
        //2.2画边框
        graphics.setColor(Color.red);
        graphics.drawRect(0,0,width-1,height-1); //drawRect方法可以画边框
        //2.3  写验证码
        String str="abcdefgh";
        Random random=new Random();   //创建随机对象
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i <=4 ; i++) {   //循环四次,产生四位的验证码
            int index=random.nextInt(str.length());  //生成角标
            //随机字符
            char ch=str.charAt(index);
            sb.append(ch);
            //TODO:每个字颜色不同,位置不同
            //绘制随机字符到 graphics
            graphics.setColor(Color.BLACK); //画笔的颜色
            graphics.drawString(ch+"",width/5*i,height/2); //验证码
        }
        session.setAttribute("code",sb.toString());
        System.out.println("======================="+sb.toString());

        //随机数
        //2.4画干扰线(TODO:升级:曲线,每根线颜色不一样)
        graphics.setColor(Color.green);
        for (int i = 0; i <5 ; i++) {
            int x1=random.nextInt(width);
            int x2=random.nextInt(width);
            int y1=random.nextInt(height);
            int y2=random.nextInt(height);
            graphics.drawLine(x1,x2,y1,y2);
        }
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();   //
        ImageIO.write((RenderedImage) image,"png",byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        InputStreamResource inputStreamResource=new InputStreamResource(byteArrayInputStream);
        HttpHeaders httpHeaders=new HttpHeaders();   //
        httpHeaders.set("Pragma","No-cache");
        httpHeaders.set("Cache-Control","no-cache");
        return  ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .contentType(MediaType.IMAGE_PNG)
                .body(inputStreamResource);
    }

}
