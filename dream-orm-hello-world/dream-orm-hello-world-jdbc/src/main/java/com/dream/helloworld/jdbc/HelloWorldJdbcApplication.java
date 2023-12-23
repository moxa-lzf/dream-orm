package com.dream.helloworld.jdbc;

import com.dream.boot.share.EnableShare;
import com.dream.flex.annotation.FlexAPT;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@FlexAPT
@EnableShare(HikariDataSource.class)
@SpringBootApplication
public class HelloWorldJdbcApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloWorldJdbcApplication.class, args);
    }
}
