package com.example.hotel_service.util;

import java.time.LocalDate;

public class DateUtil {

    private DateUtil() {
    }

    public static boolean isValidDate(LocalDate start, LocalDate end) {
        return start.isBefore(end);
    }
}
