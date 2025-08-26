package com.farumazula.tinyapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Ma1iket
 **/

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(String chatId) {
        super("Chat not found with Id: " + chatId);
    }
}
