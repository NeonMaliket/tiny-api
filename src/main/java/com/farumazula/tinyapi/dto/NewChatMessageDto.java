package com.farumazula.tinyapi.dto;

import lombok.Builder;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

/**
 * @author Ma1iket
 **/

@Builder
public record NewChatMessageDto(
        String chatId,
        String prompt
) {

    public Prompt asPrompt() {
        return Prompt.builder()
                .messages(UserMessage.builder()
                        .text(prompt)
                        .build())
                .build();
    }

}
