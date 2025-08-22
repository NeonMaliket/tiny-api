package com.farumazula.tinyapi.converters;

import com.farumazula.tinyapi.entity.ChatEntryAuthor;
import org.bson.BsonString;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class BsonStringToStringConverter implements Converter<BsonString, String> {
    @Override
    public String convert(BsonString source) {
        return source.getValue();
    }
}
