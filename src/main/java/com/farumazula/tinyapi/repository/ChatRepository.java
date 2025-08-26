package com.farumazula.tinyapi.repository;

import com.farumazula.tinyapi.entity.Chat;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author Ma1iket
 **/

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
}
