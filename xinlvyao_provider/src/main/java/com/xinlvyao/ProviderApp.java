package com.xinlvyao;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//当前类是启动类
@MapperScan("com.jd.mapper")//扫描mapper，整合mybatis注解
@EnableDubbo//开启dubbo远程服务
public class ProviderApp {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApp.class,args);
    }
}
