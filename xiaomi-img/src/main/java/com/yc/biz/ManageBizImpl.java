package com.yc.biz;

import com.yc.bean.Manager;
import com.yc.bean.Phone;
import com.yc.bean.PhoneInfo;
import com.yc.beanVO.PhoneBrandVO;
import com.yc.beanVO.PhoneManageVO;
import com.yc.dao.ManageDao;
import com.yc.util.CacheService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS,
        isolation = Isolation.DEFAULT,timeout = 2000,
        readOnly = true,rollbackFor = RuntimeException.class)
@Slf4j
public class ManageBizImpl implements ManageBiz{
    @Autowired
    private ManageDao manageDao;

    @Autowired
    private CacheService cacheService;


    @Override
    public List<PhoneBrandVO> findAllManage() {
        return this.manageDao.findAllManage();
    }

    @Override
    public List<PhoneManageVO> findById(Integer pid) {
      return  this.manageDao.findById(pid);
    }



    //TODO ： 发生修改时 应清除Redis中的数据
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT,timeout = 2000,
            rollbackFor = RuntimeException.class)
    @Override
    public int updateNum(Integer num,Integer id) {
      return  this.manageDao.updateNum(num,id);
    }

    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT,timeout = 2000,
            rollbackFor = RuntimeException.class)
    @Override
    public Integer addPhone(String name, String brand) {
        Phone phone=new Phone();
        phone.setName(name);
        phone.setBrand(brand);
        this.manageDao.addPhone(phone);  //将自动生成的主键值赋予phoneEntity对象的id属性，然后通过phoneEntity.getId()获取自动生成的主键值。
        System.out.println("新增的主键id"+phone.getPid());
        return phone.getPid();
    }

    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT,timeout = 2000,
            rollbackFor = RuntimeException.class)
    @Override
    public void addPhoneInfo(String price, Integer num, String color, String version, String path, int pid) {
       PhoneInfo phoneInfo=new PhoneInfo();
       phoneInfo.setPics(path);
       phoneInfo.setCap(version);
       phoneInfo.setColor(color);
       phoneInfo.setPid(pid);
       phoneInfo.setNum(num);
       phoneInfo.setPrice(price);
       this.manageDao.addPhoneInfo(phoneInfo);
    }

    @Override
    public List<Manager> findAdmin() {
       return this.manageDao.findAdmin();
    }


}
