package com.patterns.flyWeight;

/**
 * @author cg
 * @version 1.0
 * @Date 2024/6/15 21:06
 */
public class Test {
    public static void main(String[] args) {
        showPhone phone = showPhoneFactory.getPhone("123");
        System.out.println(phone);

        showPhone phone2 = showPhoneFactory.getPhone("666");
        System.out.println(phone2);

        showPhone phone3 = showPhoneFactory.getPhone("123");
        System.out.println(phone3);
        System.out.println(phone3==phone);

    }
}
