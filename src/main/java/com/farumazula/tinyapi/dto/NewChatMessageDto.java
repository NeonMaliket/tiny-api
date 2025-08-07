package com.farumazula.tinyapi.dto;

import com.farumazula.tinyapi.entity.ChatEntry;
import com.farumazula.tinyapi.entity.ChatEntryAuthor;
import lombok.Builder;

import java.util.UUID;

/**
 * @author Ma1iket
 **/

@Builder
public record NewChatMessageDto(
        String chatId,
        String prompt
) {

    public ChatEntry buildMessage() {
        return ChatEntry.builder()
                .id(UUID.randomUUID().toString())
                .author(ChatEntryAuthor.USER)
                .content(prompt)
                .build();
    }

}
