package com.patterns.flyWeight;

import java.util.HashMap;

/** 享元模式工厂类  --核心类
 * @author cg
 * @version 1.0
 * @Date 2024/6/15 20:09
 */
public class showPhoneFactory {

    static HashMap<String,showPhone> hashMap=new HashMap<>();


    public static showPhone getPhone(String key){
        showPhone showPhone=hashMap.get(key);

        if (showPhone==null){
            showPhone=new XmPhone(key); //如果没有则新建一个
            hashMap.put(key,showPhone);//然后放入池中
        }
        return showPhone;
    }



    public void getSize(){
        int size = hashMap.size();
        System.out.println(size);
    }
}
