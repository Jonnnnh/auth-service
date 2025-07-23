package com.example.authspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AuthSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthSpringApplication.class, args);
    }

}
