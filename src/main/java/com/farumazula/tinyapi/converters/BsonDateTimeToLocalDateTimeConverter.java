package com.farumazula.tinyapi.converters;

import org.bson.BsonDateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ReadingConverter
public class BsonDateTimeToLocalDateTimeConverter implements Converter<BsonDateTime, LocalDateTime> {
    @Override
    public LocalDateTime convert(BsonDateTime source) {
        return Instant.ofEpochMilli(source.getValue())
                      .atZone(ZoneOffset.UTC)
                      .toLocalDateTime();
    }
}
