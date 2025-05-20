package com.example.HRMS.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateAndTimeUtil {

    public static LocalDateTime parseLocalDate(String dateString, boolean isEndOfDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            if (isEndOfDay) {
                return LocalDate.parse(dateString, formatter).atTime(23, 59, 59, 999999999); // End of the day
            } else {
                return LocalDate.parse(dateString, formatter).atStartOfDay(); // Start of the day
            }
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Date must be in dd-MM-yyyy format.");
        }
    }
}
