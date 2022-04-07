package com.xinlvyao;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class ReceiverApp {
    public static void main(String[] args) {
        SpringApplication.run(ReceiverApp.class,args);
    }
}
