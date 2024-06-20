package com.demo;


import com.yc.ImgApp;
import com.yc.bean.PhoneInfo;
import com.yc.biz.FileBiz;
import com.yc.util.FdfsClientWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ImgApp.class)    //要加测试单元的启动类
public class FastDfsDemo{

    @Autowired
    private FdfsClientWrapper fdfsClientWrapper;

    @Autowired
    private FileBiz fileBiz;


    //数据库中实现文件转存demo
    @Test
    public void test() throws IOException {
        String src="D:/";
        List<PhoneInfo> list= fileBiz.updateSrc();
        String value;
        String filePath;
        for (PhoneInfo phoneInfo : list) {
            value=src+phoneInfo.getPics();
            File file=new File(value);
            filePath=fdfsClientWrapper.uploadFile(file);
            System.out.println(filePath);
            //  fileBiz.updateById(phoneInfo);
            //  System.out.println(src+phoneInfo.getPics());
            phoneInfo.setPics(filePath);  //路径重新赋值
            fileBiz.updateById(phoneInfo);
        }
    }

}
