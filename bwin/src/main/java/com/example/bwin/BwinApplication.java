package com.example.bwin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@ComponentScan(basePackages = {"org.example.commons.services", "org.example.commons.bwin.services", "com.example.bwin.controllers"})
@EnableJpaRepositories(basePackages = "org.example.commons.repos")
@EntityScan(basePackages = "org.example.commons.entities")
public class BwinApplication {

    public static void main(String[] args) {
        SpringApplication.run(BwinApplication.class, args);
    }

}
