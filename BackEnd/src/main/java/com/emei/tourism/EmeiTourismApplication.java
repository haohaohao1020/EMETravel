package com.emei.tourism;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.emei.tourism.mapper")
public class EmeiTourismApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmeiTourismApplication.class, args);
        System.out.println("================ 峨眉山旅游系统启动成功 ================");
    }
}
