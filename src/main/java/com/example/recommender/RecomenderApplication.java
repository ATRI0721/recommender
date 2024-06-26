package com.example.recommender;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.recommender.mapper")
public class RecomenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecomenderApplication.class, args);
    }

}
