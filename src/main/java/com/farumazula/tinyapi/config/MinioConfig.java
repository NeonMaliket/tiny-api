package com.farumazula.tinyapi.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ma1iket
 **/

@Configuration
public class MinioConfig {

    @Value("${spring.minio.url}")
    private String springMinioUrl;
    @Value("${spring.minio.username}")
    private String springMinioUsername;
    @Value("${spring.minio.password}")
    private String springMinioPassword;


    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(springMinioUrl)
                .credentials(springMinioUsername, springMinioPassword)
                .build();
    }


}
