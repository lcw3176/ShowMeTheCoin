package com.joebrooks.showmethecoin.global.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class CalendarSerializer extends JsonSerializer<Calendar> {

    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    public static final TimeZone LOCAL_TIME_ZONE = TimeZone.getTimeZone("Asia/Seoul");

    @Override
    public void serialize(Calendar value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(FORMATTER.format(value.getTime()));
        }
    }
}