package com.farumazula.tinyapi.dto;

import com.farumazula.tinyapi.entity.Chat;

/**
 * @author Ma1iket
 **/

public record NewChatDto(
        String title
) {
    public Chat asChat() {
        return Chat.builder()
                .title(title)
                .build();
    }
}
