package com.farumazula.tinyapi.repository;

import com.farumazula.tinyapi.entity.DocumentMetadata;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author Ma1iket
 **/

public interface DocumentMetadataRepository extends ReactiveMongoRepository<DocumentMetadata, String> {
}
