package com.farumazula.tinyapi.controller;

import com.farumazula.tinyapi.dto.ChatMessageDto;
import com.farumazula.tinyapi.dto.MessageChunk;
import com.farumazula.tinyapi.dto.NewChatMessageDto;
import com.farumazula.tinyapi.service.ChatMessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @author Ma1iket
 **/

@Log4j2
@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    @PostMapping("/stream")
    public Flux<MessageChunk> streamMessage(@RequestBody NewChatMessageDto chatMessage) {
        log.info("Controller 'Streaming message for: {}'", chatMessage);
        return chatMessageService.streamMessage(chatMessage);
    }

    @GetMapping(value = "/{chatId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<ChatMessageDto>> streamChatHistory(@PathVariable String chatId) {
        log.info("Controller 'Streaming chat history for: {}'", chatId);
        return chatMessageService.streamMessages(chatId);
    }
}
