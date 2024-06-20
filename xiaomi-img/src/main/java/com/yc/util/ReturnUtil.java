package com.yc.util;

import java.util.HashMap;
import java.util.Map;

public class ReturnUtil {
    public static Map<String,Object> returnErr(String message) {
        Map<String,Object> result=new HashMap<>();
        result.put("code",0);
        result.put("msg",message);
        return result;
    }


}