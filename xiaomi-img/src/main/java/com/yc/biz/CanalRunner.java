package com.yc.biz;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author 12547
 * @version 1.0
 * @Date 2024/3/21 21:16
 */
@Component
public class CanalRunner implements CommandLineRunner {

    @Autowired
    private CanalService canalService;

    @Override
    public void run(String... args) throws Exception {
        canalService.run();
    }
}
