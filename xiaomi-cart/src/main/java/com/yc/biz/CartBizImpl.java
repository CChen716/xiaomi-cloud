package com.yc.biz;


import annotation.EnableRpc;
import com.yc.bean.CartInfo;
import com.yc.bean.OrderItemInfo;
import com.yc.bean.PhoneInfo;
import com.yc.beanVO.CartShowInfo;
import com.yc.dao.CartDao;
import com.yc.dao.OrderIteminfoDAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS,
        isolation = Isolation.DEFAULT, timeout = 2000,
        readOnly = true, rollbackFor = RuntimeException.class)
@Slf4j
public class CartBizImpl implements CartBiz{

    @Autowired
    private CartDao cartDao;

    private OrderIteminfoDAO orderIteminfoDAO;

    @Override
    public void del(String cno) {
        this.cartDao.deleteById(cno);
    }

    @Override
    public void update(int num,int cno) {

        this.cartDao.update(num,cno);
    }

    @Override
    public List<CartInfo> add(CartInfo cart) {
        return this.cartDao.add(cart);
    }

    @Override
    public void dels(int[] cnos,int ono) {
        for(int cno : cnos){
            CartInfo cart1 = this.cartDao.findByCno(cno);
            int gno = cart1.getGno();
            int num = cart1.getNum();
            PhoneInfo phoneInfo = this.cartDao.findByGno(gno);
            double price = Double.parseDouble(phoneInfo.getPrice());
            OrderItemInfo orderItemInfo = new OrderItemInfo();
            orderItemInfo.setOno(String.valueOf(ono));
            orderItemInfo.setGno(gno);
            orderItemInfo.setNum(num);
            orderItemInfo.setPrice(price);
            orderItemInfo.setStatu(2);
            this.cartDao.addorderIteminfo(orderItemInfo);
            this.cartDao.deleteById(cno);
        }
    }

    @Override
    public int totalCartNum(int uno) {
        return this.cartDao.totalCartNum(uno);
    }

    @Override
    public List<Map<String, List<CartShowInfo>>> findByUno(int uno) {
        return this.cartDao.findByUno(uno);
    }


    @EnableRpc
    public String TestRpc(String userID,String id) {
        System.out.println("RPC测试方法"+userID+id);
        return "RPC测试方法"+userID+id;
    }



}
