package com.farumazula.tinyapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ma1iket
 **/

@Data
@Document("chats")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chat {

    @Id
    private String id;
    private String title;
    @CreatedDate
    private LocalDateTime createdAt;
    @Builder.Default
    private List<ChatEntry> history = new ArrayList<>();
}
