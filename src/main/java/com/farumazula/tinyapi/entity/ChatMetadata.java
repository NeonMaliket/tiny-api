package com.farumazula.tinyapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @author Ma1iket
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMetadata {
    private String systemPrompt;
    @Indexed
    private String logoImageId;
    @Indexed
    private String backgroundImageId;
    @Builder.Default
    private double temperature = 0.3;
    @Builder.Default
    private double topP = 0.7;
    @Builder.Default
    private double topK = 20;
    @Builder.Default
    private double repeatPenalty = 1.1;
}
