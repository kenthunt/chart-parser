package com.robinhowlett.chartparser.formats;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Jackson serializer to print useful information about the {@link LocalDate} instance
 */
public class SimpleLocalDateSerializer extends LocalDateSerializer {

    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("text", localDate.toString()); // "2017-06-25"
        jsonGenerator.writeNumberField("year", localDate.getYear());
        jsonGenerator.writeNumberField("month", localDate.getMonthValue());
        jsonGenerator.writeNumberField("day", localDate.getDayOfMonth());
        jsonGenerator.writeStringField("dayOfWeek",
                localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US)); // "Sunday"
        jsonGenerator.writeNumberField("dayOfYear", localDate.getDayOfYear()); // 176

        jsonGenerator.writeEndObject();
    }
}
