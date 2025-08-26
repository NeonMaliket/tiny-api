package com.farumazula.tinyapi.entity;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Ma1iket
 **/

@Data
@Builder
@Document("chat_messages")
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    private String id;
    private String content;
    @Field(targetType = FieldType.DATE_TIME)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private ChatEntryAuthor author;
    @Indexed
    private String chatId;

    public static ChatMessage fromMessage(String chatId, Message message) {
        return ChatMessage.builder()
                .id(UUID.randomUUID().toString())
                .content(message.getText())
                .chatId(chatId)
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
