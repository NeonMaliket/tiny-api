package com.farumazula.tinyapi.controller;

import com.farumazula.tinyapi.dto.ChatDto;
import com.farumazula.tinyapi.dto.NewChatDto;
import com.farumazula.tinyapi.dto.SimpleChatDto;
import com.farumazula.tinyapi.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * @author Ma1iket
 **/

@Log4j2
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/stream")
    public Flux<ServerSentEvent<SimpleChatDto>> findAllChats() {
        log.info("findAllChats");
        return chatService.findAllChats();
    }

    @GetMapping("/{chatId}")
    public Mono<ChatDto> findChatById(@PathVariable String chatId) {
        log.debug("findChatById {}", chatId);
        return chatService.findChatById(chatId);
    }

    @PostMapping
    public Mono<SimpleChatDto> createChat(@RequestBody NewChatDto chat) {
        log.debug("createChat {}", chat);
        return chatService.createChat(chat);
    }

    @DeleteMapping("/{chatId}")
    public Mono<Void> deleteChatById(@PathVariable String chatId) {
        log.debug("deleteChatById {}", chatId);
        return chatService.deleteChat(chatId);
    }
}
