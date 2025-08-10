package com.farumazula.tinyapi.repository;

import com.farumazula.tinyapi.entity.LoadedDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Ma1iket
 **/

public interface DocumentRepository extends MongoRepository<LoadedDocument, String> {

    boolean existsByFilenameAndContentHash(
            String filename,
            String contentHash
    );

}
