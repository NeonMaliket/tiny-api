package com.farumazula.tinyapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private ChatEntryAuthor author;
}
