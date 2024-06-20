package com.yc.config;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

/**
 * FastDFS Client配置
 *
 * @author CL
 *
 */
@Configuration
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FdfsConfig {

    /**
     * 解决上传文件过大导致的问题
     */
    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factor=new MultipartConfigFactory();
        factor.setMaxRequestSize(DataSize.ofBytes(200*1048576L)); //200mb
        factor.setMaxFileSize(DataSize.ofBytes(200*1048576L)); //200mb
        return factor.createMultipartConfig();
    }


}