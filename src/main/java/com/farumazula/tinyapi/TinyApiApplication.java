package com.farumazula.tinyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoAuditing
@EnableReactiveMongoRepositories
public class TinyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinyApiApplication.class, args);
    }

}
