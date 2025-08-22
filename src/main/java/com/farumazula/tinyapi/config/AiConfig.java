package com.farumazula.tinyapi.config;

import com.farumazula.tinyapi.memories.MongoChatMemory;
import com.farumazula.tinyapi.repository.ChatRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.api.OllamaOptions;
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
    public ResourcePatternResolver resourcePatternResolver(final ResourceLoader loader) {
        return ResourcePatternUtils.getResourcePatternResolver(loader);
    }

    @Bean
    public ChatClient chatClient(final ChatClient.Builder builder) {
        return builder
                .defaultAdvisors(
                        historyAdvisor(1)
//                        SimpleLoggerAdvisor.builder()
//                                .order(3)
//                                .build(),
//                        ragAdvisor(4),
//                        SimpleLoggerAdvisor.builder()
//                                .order(5)
//                                .build()
                )
                .defaultOptions(OllamaOptions.builder()
                        .temperature(0.3)
                        .topP(0.7)
                        .topK(20)
                        .repeatPenalty(1.1)
                        .build())
                .build();
    }

    private Advisor ragAdvisor(final int order) {
        return QuestionAnswerAdvisor
                .builder(vectorStore)
                .order(order)
                .build();
    }

    private Advisor historyAdvisor(final int order) {
        return MessageChatMemoryAdvisor
                .builder(chatMemory())
                .order(order)
                .build();
    }

    private ChatMemory chatMemory() {
        return MongoChatMemory.builder()
                .maxMessages(10)
                .chatMemoryRepository(chatRepository)
                .build();
    }
}
