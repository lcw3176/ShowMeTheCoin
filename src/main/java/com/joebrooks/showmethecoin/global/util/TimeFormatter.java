package com.joebrooks.showmethecoin.global.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeFormatter {

    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.KOREA);

    public LocalDateTime parseTime(String date, String time) {
        try {
            return LocalDateTime.parse(date + time, timeFormat);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public LocalDateTime parseTime(long timeStamp) {
        try {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp),
                    TimeZone.getTimeZone(ZoneId.of("Asia/Seoul")).toZoneId());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
