package com.joebrooks.showmethecoin.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeFormatter {

    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.KOREA);

    public LocalDateTime parseTime(String date, String time){
        try{
            return LocalDateTime.parse(date + time, timeFormat);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
