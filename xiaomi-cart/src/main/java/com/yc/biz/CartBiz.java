package com.yc.biz;

import com.yc.bean.CartInfo;
import com.yc.beanVO.CartShowInfo;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface CartBiz {


    public void del(String cno);
    public void update(int num, int cno);
    public List<CartInfo> add(CartInfo cart);
    public void dels(int[] cnos,int ono);
    /**
     * 统计某个会员的购物车中商品个数
     */
    public int totalCartNum(int uno);
    /**
     * 根据会员编号查看购物车
     */
    public List<Map<String, List<CartShowInfo>>> findByUno(int uno);


    public String TestRpc(String userID,String id);
}