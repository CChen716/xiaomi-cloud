package com.patterns.flyWeight;

/**
 * @author cg
 * @version 1.0
 * @Date 2024/6/15 18:37
 */
public class XmPhone extends showPhone{

    private String pid;

    public XmPhone(String pid){
        this.pid=pid;
    }


    @Override
    public void getPid() {
        System.out.println(pid);
    }

    public void setPid(String pid){
        this.pid=pid;
    }
}
