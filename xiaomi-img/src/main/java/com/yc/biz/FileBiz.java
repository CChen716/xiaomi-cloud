package com.yc.biz;

import com.yc.bean.PhoneInfo;

import java.util.List;

public interface FileBiz {

    public List<PhoneInfo> updateSrc();

    public void updateById(PhoneInfo phoneInfo);

}
