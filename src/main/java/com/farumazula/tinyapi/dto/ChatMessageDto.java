package com.farumazula.tinyapi.dto;

import com.farumazula.tinyapi.entity.ChatEntry;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * @author Ma1iket
 **/

@Builder
public record ChatMessageDto(
        String id,
        String content,
        LocalDateTime createdAt,
        String author
) {
    public static ChatMessageDto from(ChatEntry chatEntry) {
        return ChatMessageDto.builder()
                .id(chatEntry.getId())
                .content(chatEntry.getContent())
                .createdAt(chatEntry.getCreatedAt())
                .author(chatEntry.getAuthor().getValue())
                .build();
    }
}
