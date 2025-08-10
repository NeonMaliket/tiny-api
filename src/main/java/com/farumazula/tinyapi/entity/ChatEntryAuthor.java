package com.farumazula.tinyapi.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.Optional;

/**
 * @author Ma1iket
 **/

@Getter
@RequiredArgsConstructor
public enum ChatEntryAuthor {

    USER("user") {
        @Override
        public Message buildMessage(String text) {
            return new UserMessage(text);
        }
    },
    ASSISTANT("assistant") {
        @Override
        public Message buildMessage(String text) {
            return new AssistantMessage(text);
        }
    },
    SYSTEM("system") {
        @Override
        public Message buildMessage(String text) {
            return new SystemMessage(text);
        }
    };

    private final String value;

    public abstract Message buildMessage(String text);

    public static Optional<ChatEntryAuthor> fromValue(String value) {
        for (ChatEntryAuthor chatEntryAuthor : ChatEntryAuthor.values()) {
            if (chatEntryAuthor.value.equals(value)) {
                return Optional.of(chatEntryAuthor);
            }
        }
        return Optional.empty();
    }
}
