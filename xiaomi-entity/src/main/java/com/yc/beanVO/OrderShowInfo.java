package com.yc.beanVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderShowInfo implements Serializable {
    private int ino;
    private int name;
    private int color;
    private int cap;
    private int num;
    private String pics;
}
