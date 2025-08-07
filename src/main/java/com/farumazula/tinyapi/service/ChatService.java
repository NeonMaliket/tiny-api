package com.farumazula.tinyapi.service;

import com.farumazula.tinyapi.dto.*;
import com.farumazula.tinyapi.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public Optional<ChatMessageDto> sendPrompt(NewChatMessageDto newChatMessageDto) {
        log.info("Sending chat prompt {}", newChatMessageDto);
        var chat = chatRepository.findById(newChatMessageDto.chatId());
        if (chat.isPresent()) {
            var localChat = chat.get();
            var message = newChatMessageDto.buildMessage();
            localChat.addMessage(message);
            chatRepository.save(localChat);
            return Optional.of(ChatMessageDto.from(message));
        }
        return Optional.empty();
    }
}
