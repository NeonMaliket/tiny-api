package com.farumazula.tinyapi.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ma1iket
 **/

@Configuration
public class AiConfig {


    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient
                .builder(chatModel)
                .build();
    }

}
