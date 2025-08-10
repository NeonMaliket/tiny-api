package com.farumazula.tinyapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author Ma1iket
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("loaded_document")
public class LoadedDocument {

    @Id
    private String id;
    private String filename;
    private String contentHash;
    private String documentType;
    private int chunkCount;
    @CreatedDate
    private LocalDateTime createdAt;
}
