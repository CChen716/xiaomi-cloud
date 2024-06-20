package com.yc.biz;

import com.yc.bean.PhoneInfo;
import com.yc.dao.FileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileBizImpl implements FileBiz{

    @Autowired
    private FileDao fileDao;


    @Override
    public List<PhoneInfo> updateSrc() {
        return this.fileDao.findPics();
    }


    @Override
    public void updateById(PhoneInfo phoneInfo) {
         this.fileDao.updateById(phoneInfo);
    }

}
