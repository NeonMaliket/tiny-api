package com.farumazula.tinyapi.dto;

import com.farumazula.tinyapi.entity.Chat;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * @author Ma1iket
 **/

@Builder
public record SimpleChatDto(
        String id,
        String title,
        LocalDateTime createdAt
) {

    public static SimpleChatDto from(Chat chat) {
        return SimpleChatDto.builder()
                .id(chat.getId())
                .title(chat.getTitle())
                .createdAt(chat.getCreatedAt())
                .build();
    }

}
