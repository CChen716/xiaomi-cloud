package com.yc.config;

/**
 * ES的静态常量接口
 */
public interface EsConfig {
    //索引名称
    String index="xiaomi-phone";
    //单个文档添加超时时间
    String DTimeOut="1s";
    //批量文档添加超时时间
    String DSTimeOut="10s";
}
