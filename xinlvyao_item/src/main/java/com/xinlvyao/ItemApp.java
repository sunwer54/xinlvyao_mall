package com.xinlvyao;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableDubbo
@EnableCaching//开启redis缓存功能
public class ItemApp {
    public static void main(String[] args) {
        SpringApplication.run(ItemApp.class,args);
    }
}
