package com.farumazula.tinyapi.service;

import com.farumazula.tinyapi.dto.*;
import com.farumazula.tinyapi.entity.Chat;
import com.farumazula.tinyapi.entity.ChatMessage;
import com.farumazula.tinyapi.events.StreamMessagesEvent;
import com.farumazula.tinyapi.repository.ChatMessageRepository;
import com.farumazula.tinyapi.repository.ChatRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

/**
 * @author Ma1iket
 **/

@Log4j2
@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public Flux<SimpleChatDto> findAllChats() {
        log.debug("findAllChats");
        return chatRepository.findAll(
                Sort.by(
                        Sort.Direction.DESC,
                        "createdAt")
        ).map(SimpleChatDto::from);
    }

    public Mono<ChatDto> findChatById(String id) {
        log.debug("findChatById");
        return chatRepository.findById(id).map(ChatDto::from);
    }

    public Mono<SimpleChatDto> createChat(NewChatDto newChatDto) {
        log.info("Creating chat {}", newChatDto);
        var saved = chatRepository.save(newChatDto.asChat());
        return saved.map(SimpleChatDto::from);
    }

    public Mono<Void> deleteChat(String chatId) {
        log.info("Deleting chat with id: {}", chatId);
        return chatRepository
                .deleteById(chatId)
                .flatMap(v -> chatMessageRepository.deleteAllByChatId(chatId));
    }
}

