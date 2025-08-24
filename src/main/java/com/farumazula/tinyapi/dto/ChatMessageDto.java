package com.farumazula.tinyapi.dto;

import com.farumazula.tinyapi.entity.ChatMessage;
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
    public static ChatMessageDto from(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
                .id(chatMessage.getId())
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt())
                .author(chatMessage.getAuthor().getValue())
                .build();
    }
}
