package com.farumazula.tinyapi.repository;

import com.farumazula.tinyapi.entity.ChatMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Ma1iket
 **/

public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {

    Flux<ChatMessage> findByChatId(String chatId);

    Mono<Void> deleteAllByChatId(String chatId);

}
