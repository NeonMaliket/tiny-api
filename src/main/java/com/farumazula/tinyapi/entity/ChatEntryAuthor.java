package com.farumazula.tinyapi.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * @author Ma1iket
 **/

@Getter
@RequiredArgsConstructor
public enum ChatEntryAuthor {

    USER("user"),
    ASSISTANT("assistant");

    private final String value;

    public static Optional<ChatEntryAuthor> fromValue(String value) {
        for (ChatEntryAuthor chatEntryAuthor : ChatEntryAuthor.values()) {
            if (chatEntryAuthor.value.equals(value)) {
                return Optional.of(chatEntryAuthor);
            }
        }
        return Optional.empty();
    }
}
