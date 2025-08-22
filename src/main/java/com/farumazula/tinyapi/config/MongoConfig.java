package com.farumazula.tinyapi.config;

import com.farumazula.tinyapi.converters.BsonDateTimeToLocalDateTimeConverter;
import com.farumazula.tinyapi.converters.BsonStringToChatEntryAuthorConverter;
import com.farumazula.tinyapi.converters.BsonStringToStringConverter;
import com.farumazula.tinyapi.converters.LocalDateTimeToBsonDateTimeConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        List<Object> converters = new ArrayList<>();
        converters.add(new BsonDateTimeToLocalDateTimeConverter());
        converters.add(new LocalDateTimeToBsonDateTimeConverter());
        converters.add(new BsonStringToChatEntryAuthorConverter());
        converters.add(new BsonStringToStringConverter());
        return new MongoCustomConversions(converters);
    }
}
