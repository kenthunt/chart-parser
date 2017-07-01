package com.robinhowlett.chartparser.formats;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Jackson deserializer to convert a JSON date-related object created by
 * {@link SimpleLocalDateSerializer} back to a {@link LocalDate} instance
 */
public class SimpleLocalDateDeserializer extends LocalDateDeserializer {

    public SimpleLocalDateDeserializer() {
        super(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext
            deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        int year = (int) node.get("year").numberValue();
        int month = (int) node.get("month").numberValue();
        int day = (int) node.get("day").numberValue();

        return LocalDate.of(year, month, day);
    }
}
