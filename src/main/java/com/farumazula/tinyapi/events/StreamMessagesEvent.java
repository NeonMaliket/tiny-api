package com.farumazula.tinyapi.events;

import com.farumazula.tinyapi.dto.ChatMessageDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;

/**
 * @author Ma1iket
 **/

@Getter
@RequiredArgsConstructor
public enum StreamMessagesEvent {
    HISTORY("history"),
    NEW_MESSAGE("new_message");

    private final String value;

    public ServerSentEvent<ChatMessageDto> toSentEvent(ChatMessageDto chatMessageDto) {
        return ServerSentEvent.builder(chatMessageDto)
                .event(value)
                .build();
    }
}
