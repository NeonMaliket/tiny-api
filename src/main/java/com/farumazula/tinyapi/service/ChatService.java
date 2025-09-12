package com.farumazula.tinyapi.service;

import com.farumazula.tinyapi.dto.ChatDto;
import com.farumazula.tinyapi.dto.NewChatDto;
import com.farumazula.tinyapi.dto.SimpleChatDto;
import com.farumazula.tinyapi.entity.Chat;
import com.farumazula.tinyapi.entity.ChatMetadata;
import com.farumazula.tinyapi.events.StreamEvent;
import com.farumazula.tinyapi.repository.ChatMessageRepository;
import com.farumazula.tinyapi.repository.ChatRepository;
import com.mongodb.client.model.changestream.OperationType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Ma1iket
 **/

@Log4j2
@Service
public class ChatService {

    @Autowired
    private ReactiveMongoTemplate mongo;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public Flux<ServerSentEvent<SimpleChatDto>> findAllChats() {
        log.debug("findAllChats");
        var history = chatRepository.findAll(
                Sort.by(
                        Sort.Direction.DESC,
                        "createdAt")
        ).map(SimpleChatDto::from).map(StreamEvent.HISTORY::toSentEvent);
        var live = mongo
                .changeStream(Chat.class)
                .watchCollection(Chat.class)
                .listen()
                .filter(x -> x.getOperationType() == OperationType.DELETE || x.getOperationType() == OperationType.INSERT)
                .map(entry -> {
                    var body = entry.getBody();
                    if (entry.getOperationType() != OperationType.DELETE && body != null) {
                        return StreamEvent.NEW.toSentEvent(SimpleChatDto.from(body));
                    } else {
                        return StreamEvent.DELETE.toSentEvent(SimpleChatDto.builder()
                                .id(entry.getRaw().getDocumentKey().get("_id").asObjectId().getValue().toString())
                                .build());
                    }
                });
        return Flux.concat(history, live);
    }

    public Mono<ChatDto> findChatById(String id) {
        log.debug("findChatById");
        return chatRepository.findById(id).map(ChatDto::from);
    }

    public Mono<SimpleChatDto> createChat(NewChatDto newChatDto) {
        log.info("Creating chat {}", newChatDto);
        var chat = newChatDto.asChat();
        chat.setMetadata(ChatMetadata.builder().build());
        var saved = chatRepository.save(chat);
        return saved.map(SimpleChatDto::from);
    }

    public Mono<Void> deleteChat(String chatId) {
        log.info("Deleting chat with id: {}", chatId);
        return chatRepository
                .deleteById(chatId)
                .then(chatMessageRepository.deleteAllByChatId(chatId));
    }
}

