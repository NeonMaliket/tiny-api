package com.farumazula.tinyapi.service;

import com.farumazula.tinyapi.dto.*;
import com.farumazula.tinyapi.entity.ChatEntryAuthor;
import com.farumazula.tinyapi.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public void addChatEntry(String chatId, ChatMessageDto chatMessageDto) {
        log.debug("addChatEntry");
        var entry = chatMessageDto.toEntry();
        entry.setId(UUID.randomUUID().toString());
        chatRepository.addChatEntry(chatId, entry);
    }

    public void deleteChat(String chatId) {
        log.info("Deleting chat with id: {}", chatId);
        chatRepository.deleteById(chatId);
    }

    public SseEmitter proceedInteraction(NewChatMessageDto newChatMessageDto) {
        log.info("Sending chat prompt {}", newChatMessageDto);
        var sseEmitter = new SseEmitter();
        var chatResponse = new StringBuffer();
        var chatId = newChatMessageDto.chatId();
        addChatEntry(chatId, ChatMessageDto.from(newChatMessageDto.asUserMessage()));

        chatClient.prompt(newChatMessageDto.prompt())
                .stream()
                .chatClientResponse()
                .subscribe(
                        (response) -> processToken(response.chatResponse(), sseEmitter, chatResponse),
                        sseEmitter::completeWithError,
                        () -> {
                            var assistantMessage = ChatMessageDto.builder()
                                    .author(ChatEntryAuthor.ASSISTANT.getValue())
                                    .content(chatResponse.toString())
                                    .build();

                            addChatEntry(chatId, assistantMessage);
                        }
                );
        return sseEmitter;
    }

    @SneakyThrows
    private void processToken(ChatResponse response, SseEmitter sseEmitter, StringBuffer responseBuilder) {
        var resp = response.getResult().getOutput().getText();
        if (resp != null) {
            sseEmitter.send(resp);
            responseBuilder.append(resp);
        }
    }
}
