package com.farumazula.tinyapi.service;

import com.farumazula.tinyapi.dto.ChatDto;
import com.farumazula.tinyapi.dto.NewChatDto;
import com.farumazula.tinyapi.dto.NewChatMessageDto;
import com.farumazula.tinyapi.dto.SimpleChatDto;
import com.farumazula.tinyapi.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Ma1iket
 **/

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatClient chatClient;

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
        return chatRepository.deleteById(chatId);
    }

    public Flux<String> proceedInteraction(NewChatMessageDto newChatMessageDto) {
        log.info("Sending chat prompt {}", newChatMessageDto);
        var chatId = newChatMessageDto.chatId();

        return chatClient.prompt(newChatMessageDto.prompt())
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .chatResponse()
                .map(response -> {
                    if (response.getResult() == null ||
                            response.getResult().getOutput() == null ||
                            response.getResult().getOutput().getText() == null) {
                        return "";
                    }
                    return response.getResult().getOutput().getText();
                });
    }
}

