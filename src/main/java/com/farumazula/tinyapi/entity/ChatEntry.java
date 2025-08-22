package com.farumazula.tinyapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.BsonDateTime;
import org.bson.Document;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

/**
 * @author Ma1iket
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatEntry {
    private String id;
    private String content;
    @Field(targetType = FieldType.DATE_TIME)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private ChatEntryAuthor author;

    public static ChatEntry fromMessage(Message message) {
        return ChatEntry.builder()
                .id(UUID.randomUUID().toString())
                .content(message.getText())
                .author(ChatEntryAuthor.fromValue(
                                message
                                        .getMessageType()
                                        .getValue())
                        .orElseThrow())
                .build();
    }

    public Message toMessage() {
        return author.buildMessage(content);
    }
}
