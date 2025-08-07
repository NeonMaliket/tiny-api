package com.farumazula.tinyapi.dto;

import com.farumazula.tinyapi.entity.Chat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Ma1iket
 **/

@Builder
public record ChatDto(
        String id,
        String title,
        LocalDateTime createdAt,
        List<ChatMessageDto> history
) {

    public static ChatDto from(Chat chat) {
        var history = chat.getHistory()
                .stream()
                .map(ChatMessageDto::from)
                .toList();
        return ChatDto.builder()
                .id(chat.getId())
                .title(chat.getTitle())
                .createdAt(chat.getCreatedAt())
                .history(history)
                .build();
    }

}
