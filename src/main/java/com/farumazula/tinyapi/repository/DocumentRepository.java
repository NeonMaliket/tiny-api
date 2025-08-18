package com.farumazula.tinyapi.repository;

import com.farumazula.tinyapi.entity.LoadedDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * @author Ma1iket
 **/

public interface DocumentRepository extends ReactiveMongoRepository<LoadedDocument, String> {

    Mono<Boolean> existsByFilenameAndContentHash(
            String filename,
            String contentHash
    );

}
