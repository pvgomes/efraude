package com.efraude;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EfraudeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EfraudeApplication.class, args);
    }
}
