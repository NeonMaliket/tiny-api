package com.farumazula.tinyapi.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;

/**
 * @author Ma1iket
 **/

@Getter
@RequiredArgsConstructor
public enum StreamEvent {
    HISTORY("history"),
    DELETE("delete"),
    UNSUPPORTED("unsupported"),
    NEW("new");

    private final String value;

    public <T> ServerSentEvent<T> toSentEvent(T obj) {
        return ServerSentEvent.builder(obj)
                .event(value)
                .build();
    }
}
