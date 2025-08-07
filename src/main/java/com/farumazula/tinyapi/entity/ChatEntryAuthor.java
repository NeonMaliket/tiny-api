package com.farumazula.tinyapi.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Ma1iket
 **/

@Getter
@RequiredArgsConstructor
public enum ChatEntryAuthor {

    USER("user"),
    ASSISTANT("assistant");

    private final String value;
}
