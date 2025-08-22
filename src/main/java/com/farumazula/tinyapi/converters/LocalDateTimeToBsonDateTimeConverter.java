package com.farumazula.tinyapi.converters;

import org.bson.BsonDateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@WritingConverter
public class LocalDateTimeToBsonDateTimeConverter implements Converter<LocalDateTime, BsonDateTime> {
    @Override
    public BsonDateTime convert(LocalDateTime source) {
        return new BsonDateTime(source.atZone(ZoneOffset.UTC)
                                      .toInstant()
                                      .toEpochMilli());
    }
}
