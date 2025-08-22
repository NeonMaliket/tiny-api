package com.farumazula.tinyapi.converters;

import com.farumazula.tinyapi.entity.ChatEntryAuthor;
import org.bson.BsonString;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class BsonStringToChatEntryAuthorConverter implements Converter<BsonString, ChatEntryAuthor> {
    @Override
    public ChatEntryAuthor convert(BsonString source) {
        return ChatEntryAuthor.valueOf(source.getValue());
    }
}
