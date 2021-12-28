package com.example.bets22;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@ComponentScan(basePackages ={"org.example.commons.services", "org.example.commons.bets22.services", "com.example.bets22.controllers"})
@EnableJpaRepositories(basePackages="org.example.commons.repos")
@EntityScan(basePackages ="org.example.commons.entities")
public class Bets22Application {

    public static void main(String[] args) {
        SpringApplication.run(Bets22Application.class, args);
    }

}
