package com.farumazula.tinyapi.dto;

import lombok.Builder;

/**
 * @author Ma1iket
 **/

@Builder
public record NewChatMessageDto(
        String chatId,
        String prompt
) {

}
