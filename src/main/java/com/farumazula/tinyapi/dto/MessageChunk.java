package com.farumazula.tinyapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

/**
 * @author Ma1iket
 **/

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MessageChunk(
        String chunk,
        boolean isLast) {
}
