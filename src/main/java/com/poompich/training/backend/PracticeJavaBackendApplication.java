package com.poompich.training.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PracticeJavaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracticeJavaBackendApplication.class, args);
    }

}
