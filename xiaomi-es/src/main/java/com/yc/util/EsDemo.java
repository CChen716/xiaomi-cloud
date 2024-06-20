package com.yc.util;

import com.yc.biz.EsService;
import com.yc.config.EsConfig;
import com.yc.dao.EsPhoneDao;
import com.yc.entity.EsPhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
  一些操作demo
 */
@Component
public class EsDemo {

    @Autowired
    private EsPhoneDao esPhoneDao;

    @Autowired
    private EsService esService;


    //将数据库中数据导入到es中的demo
    public void addPhone(){
        List<EsPhone> list = esPhoneDao.findAll();
        Map<String,Object> map=new HashMap<>();
        for (EsPhone esPhone : list) {
            map.put(String.valueOf(esPhone.getId()),esPhone);
        }
        esService.bulkByIdDocument(map, EsConfig.index);
    }
}
