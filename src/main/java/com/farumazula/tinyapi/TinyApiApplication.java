package com.farumazula.tinyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class TinyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinyApiApplication.class, args);
    }

}
