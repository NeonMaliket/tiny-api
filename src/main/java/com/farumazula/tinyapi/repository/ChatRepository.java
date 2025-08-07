package com.farumazula.tinyapi.repository;

import com.farumazula.tinyapi.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

/**
 * @author Ma1iket
 **/

public interface ChatRepository extends MongoRepository<Chat, String> {

    @Query(value = "{}", sort = "{ 'createdAt' : -1 }")
    Optional<Chat> findLastChat();

}
