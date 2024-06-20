package com.patterns.template;

/**  普通用户
 * @author cg
 * @version 1.0
 * @Date 2024/6/14 20:53
 */
public class BasicUser extends User{


    @Override
    public Integer computeVip() {
        return 0;
    }

    @Override
    public Integer computeSvip() {
        return 0;
    }
}
