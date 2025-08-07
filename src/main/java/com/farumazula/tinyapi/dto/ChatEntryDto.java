package com.farumazula.tinyapi.dto;

import com.farumazula.tinyapi.entity.ChatEntry;
import com.farumazula.tinyapi.entity.ChatEntryAuthor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * @author Ma1iket
 **/

@Builder
public record ChatEntryDto(
        String id,
        String content,
        LocalDateTime createdAt,
        ChatEntryAuthor author
) {
    public static ChatEntryDto from(ChatEntry chatEntry) {
        return ChatEntryDto.builder()
                .id(chatEntry.getId())
                .content(chatEntry.getContent())
                .createdAt(chatEntry.getCreatedAt())
                .author(chatEntry.getAuthor())
                .build();
    }
}
