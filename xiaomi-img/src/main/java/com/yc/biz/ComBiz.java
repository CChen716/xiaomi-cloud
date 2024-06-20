package com.yc.biz;


import com.yc.bean.Com;
import com.yc.bean.Phone;

import java.util.List;
import java.util.Map;

public interface ComBiz {
    public Map<Integer, List<Com>> findComByForeignId(Integer pid );
    public int addCom(Com com);
    public List<Phone> findPhoneByPid(Integer pid);

}