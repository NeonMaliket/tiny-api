package com.farumazula.tinyapi.service;

import com.farumazula.tinyapi.dto.ChatDto;
import com.farumazula.tinyapi.dto.NewChatDto;
import com.farumazula.tinyapi.dto.NewChatMessageDto;
import com.farumazula.tinyapi.dto.SimpleChatDto;
import com.farumazula.tinyapi.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

/**
 * @author Ma1iket
 **/

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatClient chatClient;

    public List<SimpleChatDto> findAllChats() {
        log.debug("findAllChats");
        return chatRepository.findAll(
                        Sort.by(
                                Sort.Direction.DESC,
                                "createdAt")
                )
                .stream()
                .map(SimpleChatDto::from)
                .toList();
    }

    public Optional<ChatDto> findLastChat() {
        log.debug("findLastChat");
        return chatRepository.findTopByOrderByCreatedAtDesc().map(ChatDto::from);
    }

    public Optional<ChatDto> findChatById(String id) {
        log.debug("findChatById");
        return chatRepository.findById(id).map(ChatDto::from);
    }

    public SimpleChatDto createChat(NewChatDto newChatDto) {
        log.info("Creating chat {}", newChatDto);
        var saved = chatRepository.save(newChatDto.asChat());
        return SimpleChatDto.from(saved);
    }

    public void deleteChat(String chatId) {
        log.info("Deleting chat with id: {}", chatId);
        chatRepository.deleteById(chatId);
    }

    public SseEmitter proceedInteraction(NewChatMessageDto newChatMessageDto) {
        log.info("Sending chat prompt {}", newChatMessageDto);
        var sseEmitter = new SseEmitter(0L);
        sseEmitter.onCompletion(() -> log.info("Chat interaction completed"));
        var chatResponse = new StringBuilder();
        var chatId = newChatMessageDto.chatId();

        chatClient.prompt(newChatMessageDto.asPrompt())
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .chatResponse()
                .subscribe(
                        response -> {
                            log.info("Chat response {}", response);
                            processToken(response, sseEmitter, chatResponse);
                        },
                        sseEmitter::completeWithError,
                        sseEmitter::complete
                );
        return sseEmitter;
    }

    @SneakyThrows
    private void processToken(ChatResponse response, SseEmitter sseEmitter, StringBuilder responseBuilder) {
        var resp = response.getResult().getOutput().getText();
        if (resp != null) {
            sseEmitter.send(resp);
            responseBuilder.append(resp);
        } else {
            log.warn("No response from chat service");
        }
    }
}
