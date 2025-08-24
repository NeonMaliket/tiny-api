package com.farumazula.tinyapi.repository;

import com.farumazula.tinyapi.entity.Chat;
import com.farumazula.tinyapi.entity.ChatMessage;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Update;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Ma1iket
 **/

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {

    @Query("{ _id: ?0 }")
    @Update("{ $push: { history: {$each: ?1} } }")
    Mono<Void> addChatEntries(String chatId, List<ChatMessage> chatMessage);
}
