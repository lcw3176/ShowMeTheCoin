package com.joebrooks.showmethecoin.global.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class CalendarDeserializer extends JsonDeserializer<Calendar> {

    @Override
    public Calendar deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException {
        String dateAsString = jsonparser.getText();
        try {
            Date date = CalendarSerializer.FORMATTER.parse(dateAsString);
            Calendar calendar = Calendar.getInstance(
                    CalendarSerializer.LOCAL_TIME_ZONE
            );
            calendar.setTime(date);

            return calendar;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}