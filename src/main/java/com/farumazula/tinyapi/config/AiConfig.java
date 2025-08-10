package com.farumazula.tinyapi.config;

import com.farumazula.tinyapi.memories.MongoChatMemory;
import com.farumazula.tinyapi.repository.ChatRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

/**
 * @author Ma1iket
 **/

@Configuration
public class AiConfig {

    @Lazy
    @Autowired
    private ChatRepository chatRepository;

    @Lazy
    @Autowired
    private VectorStore vectorStore;

    @Bean
    @Primary
    public ResourcePatternResolver resourcePatternResolver(ResourceLoader loader) {
        return ResourcePatternUtils.getResourcePatternResolver(loader);
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultAdvisors(
                        historyAdvisor(),
                        ragAdvisor())
                .build();
    }

    private Advisor ragAdvisor() {
        return QuestionAnswerAdvisor
                .builder(vectorStore)
                .build();
    }

    private Advisor historyAdvisor() {
        return MessageChatMemoryAdvisor
                .builder(chatMemory())
                .build();
    }

    private ChatMemory chatMemory() {
        return MongoChatMemory.builder()
                .chatMemoryRepository(chatRepository)
                .build();
    }
}
