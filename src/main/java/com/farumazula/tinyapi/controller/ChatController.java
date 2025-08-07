package com.farumazula.tinyapi.controller;

import com.farumazula.tinyapi.dto.ChatDto;
import com.farumazula.tinyapi.dto.NewChatDto;
import com.farumazula.tinyapi.dto.SimpleChatDto;
import com.farumazula.tinyapi.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ma1iket
 **/

@Log4j2
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/all")
    public ResponseEntity<List<SimpleChatDto>> findAllChats() {
        log.debug("findAllChats");
        return ResponseEntity.ok(chatService.findAllChats());
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDto> findChatById(@PathVariable String chatId) {
        log.debug("findChatById {}", chatId);
        return chatService.findChatById(chatId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/last")
    public ResponseEntity<ChatDto> findLastChat() {
        log.debug("findLastChat");
        return chatService.findLastChat()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SimpleChatDto> createChat(@RequestBody NewChatDto chat) {
        log.debug("createChat {}", chat);
        return ResponseEntity.ok(chatService.createChat(chat));
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChatById(@PathVariable String chatId) {
        log.debug("deleteChatById {}", chatId);
        chatService.deleteChat(chatId);
        return ResponseEntity.ok().build();
    }
}
