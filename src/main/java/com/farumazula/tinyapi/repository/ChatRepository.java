package com.farumazula.tinyapi.repository;

import com.farumazula.tinyapi.entity.Chat;
import com.farumazula.tinyapi.entity.ChatEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.Optional;

/**
 * @author Ma1iket
 **/

public interface ChatRepository extends MongoRepository<Chat, String> {

    Optional<Chat> findTopByOrderByCreatedAtDesc();

    @Query("{ _id: ?0 }")
    @Update("{ $push: { history: ?1 } }")
    void addChatEntry(String chatId, ChatEntry chatEntry);
}
