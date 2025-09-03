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
        String filename,
        String type,
        LocalDateTime createdAt
) {

    public static DocumentMetadataDto from(DocumentMetadata metadata) {
        return DocumentMetadataDto.builder()
                .id(metadata.getId())
                .filename(metadata.getFileName())
                .type(metadata.getType())
                .createdAt(metadata.getCreatedAt())
                .build();
    }

}
