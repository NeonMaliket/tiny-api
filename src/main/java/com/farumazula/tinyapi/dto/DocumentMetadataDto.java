package com.farumazula.tinyapi.dto;

import com.farumazula.tinyapi.entity.DocumentMetadata;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * @author Ma1iket
 **/

@Builder
public record DocumentMetadataDto(
        String id,
        String fileName,
        String type,
        LocalDateTime createdAt
) {

    public static DocumentMetadataDto fromDocumentMetadata(DocumentMetadata metadata) {
        return DocumentMetadataDto.builder()
                .id(metadata.getId())
                .fileName(metadata.getFileName())
                .type(metadata.getType())
                .createdAt(metadata.getCreatedAt())
                .build();
    }

}
