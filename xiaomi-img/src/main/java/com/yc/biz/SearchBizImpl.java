package com.yc.biz;

import com.yc.beanVO.PhoneBrandVO;
import com.yc.beanVO.SearchBottomVO;
import com.yc.dao.SearchDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchBizImpl implements SearchBiz{
    @Autowired
    private SearchDao searchDao;

    @Override
    public List<PhoneBrandVO> findByBrand(String brand,String name) {
        name="%"+name+"%";
        int total=this.searchDao.totalPage(brand,name);
        System.out.println(total);
        return this.searchDao.findByBrand(brand,name);
    }

    @Override
    public int findTotal(String brand, String name) {
        name="%"+name+"%";
        return this.searchDao.totalPage(brand,name);
    }

    @Override
    public List<SearchBottomVO> findBottom() {
        return this.searchDao.findBottom();
    }


}
