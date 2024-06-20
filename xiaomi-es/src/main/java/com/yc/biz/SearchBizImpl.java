package com.yc.biz;

import com.yc.beanVO.PhoneBrandVO;
import com.yc.beanVO.SearchBottomVO;
import com.yc.entity.EsPhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SearchBizImpl implements SearchBiz{


    @Autowired
    private EsService esService; //ES操作业务类

    /**
     * 搜索关键字
     * @param searchWord
     * @return
     */
    @Override
    public List<EsPhone> findByBrand(String searchWord,Integer pageSize,Integer pageNo) {
        List<EsPhone> list=null;
        try {
            list=esService.search(searchWord,pageSize,pageNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    //此处查询总数是用做分页
    @Override
    public int findTotal(String brand, String name) {
            return 0;
    }




}
