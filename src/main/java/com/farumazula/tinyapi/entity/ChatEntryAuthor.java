package com.farumazula.tinyapi.entity;

import lombok.RequiredArgsConstructor;

/**
 * @author Ma1iket
 **/

@RequiredArgsConstructor
public enum ChatEntryAuthor {

    USER("user"),
    ASSISTANT("assistant");

    private final String value;
}
