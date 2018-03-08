package com.songzi.channel.service.util;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class DateUtil {

    public static Instant getStartOfDay(LocalDate date){
        Instant instant = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        return instant;
    }

    public static Instant getEndOfDay(LocalDate date){
        Instant instant = date.plusDays(1).atStartOfDay().minusNanos(1).toInstant(ZoneOffset.UTC);
        return instant;
    }

}
