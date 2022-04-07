package com.xinlvyao;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableDubbo
@EnableRedisHttpSession(redisNamespace = "tbuser")//设置存入redis中session的前缀
public class CartApp {
    public static void main(String[] args) {
        SpringApplication.run(CartApp.class,args);
    }
}
