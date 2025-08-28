package com.farumazula.tinyapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

/**
 * @author Ma1iket
 **/

@Data
@Builder
@Document("document_metadata")
@NoArgsConstructor
@AllArgsConstructor
public class DocumentMetadata {

    @Id
    private String id;
    private String fileName;
    private String type;
    private String bucket;
    @Field(targetType = FieldType.DATE_TIME)
    @CreatedDate
    private LocalDateTime createdAt;

}
