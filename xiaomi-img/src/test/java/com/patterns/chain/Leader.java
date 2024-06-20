package com.patterns.chain;

/**  抽象处理者
 * @author cg
 * @version 1.0
 * @Date 2024/6/13 15:32
 */
public abstract class Leader {

    private String name;//姓名--级别

    protected Leader successor;  //后继者 -上级


    public Leader(String name){
        this.name=name;
    }

    public void setSuccessor(Leader leader){
        this.successor=leader;
    }

    public abstract void handleRequest(Order order);


}
