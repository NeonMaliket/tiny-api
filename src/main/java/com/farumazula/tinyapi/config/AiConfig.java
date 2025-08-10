package com.farumazula.tinyapi.config;

import com.farumazula.tinyapi.memories.MongoChatMemory;
import com.farumazula.tinyapi.repository.ChatRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ma1iket
 **/

@Configuration
public class AiConfig {

    @Autowired
    private ChatRepository chatRepository;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultAdvisors(advisor())
                .build();
    }

    private Advisor advisor() {
        return MessageChatMemoryAdvisor
                .builder(chatMemory())
                .build();
    }

    private ChatMemory chatMemory() {
        return MongoChatMemory.builder()
                .maxMessages(2)
                .chatMemoryRepository(chatRepository)
                .build();
    }
}
