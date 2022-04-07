package com.xinlvyao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jd.mapper")
public class SearchApp {
    public static void main(String[] args) {
        SpringApplication.run(SearchApp.class);
    }
}
