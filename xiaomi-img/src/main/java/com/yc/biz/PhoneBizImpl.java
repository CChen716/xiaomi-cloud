package com.yc.biz;

import com.yc.bean.Phone;
import com.yc.beanVO.PhoneSlideVO;
import com.yc.beanVO.SearchTopVO;
import com.yc.dao.PhoneDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PhoneBizImpl implements PhoneBiz{
    @Autowired
    private PhoneDao phoneDao;

    //首页5品牌栏方法
    @Override
    public List<PhoneSlideVO> findBrandSlide() {
       return this.phoneDao.findByBrand();
    }

    @Override
    public List<SearchTopVO> findAll() {
        return this.phoneDao.findAll();
    }

    @Override
    public Map<String, Object> getPhoneTop(String brand) {
        Map<String,Object> map=new HashMap<>();
        List<Phone> list=phoneDao.findTop(brand);
        map.put("list",list);
        return map;
    }

    @Override
    public  Map<String,Integer> getBrandTotal() {
        List<Phone> brandTotal = phoneDao.getBrandTotal();

        Map<String,Integer>totalMap=new HashMap<>();
        if (brandTotal==null){
            totalMap.put("code",0);
            totalMap.put("msg",0);
            return totalMap;
        }


        for (Phone phone:brandTotal){
            if (totalMap.containsKey(phone.getBrand())){
                Integer num=totalMap.get(phone.getBrand());
                num+=phone.getNum();
                totalMap.replace(phone.getBrand(),num);
            }else{
                totalMap.put(phone.getBrand(),phone.getNum());
            }
        }
        List<Phone> brand = phoneDao.findBrand();
        for (Phone phone :brand){
            if (!totalMap.containsKey(phone.getBrand())){
                totalMap.put(phone.getBrand(),0);
            }
        }

        return totalMap;
    }
    @Override
    public Map<String, Integer> getCountBrand() {
        Map<String,Integer>totalMap=new HashMap<>();
        List<Phone> countBrand = phoneDao.getCountBrand();
        for (Phone phone:countBrand){
            totalMap.put(phone.getBrand(),phone.getNum());
        }
        return totalMap;
    }
}
