package com.farumazula.tinyapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author Ma1iket
 **/

@Data
@Document("chat_entries")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatEntry {
    @Id
    private String id;
    private String content;
    @CreatedDate
    private LocalDateTime createdAt;
    private ChatEntryAuthor author;
}
