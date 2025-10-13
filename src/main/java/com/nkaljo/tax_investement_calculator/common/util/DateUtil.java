package com.nkaljo.tax_investement_calculator.common.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

    private DateUtil() {}

    public static LocalDate convertToDate(String inputDate) {
        return LocalDate.parse(inputDate, formatter);
    }

    public static String convertToString(LocalDate date) {
        return date.format(formatter);
    }

    public static LocalDate getFirstWorkingDay(LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return date.plusDays(2);
        } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return date.plusDays(1);
        }
        return date;
    }

    public static String getFirstWorkingDay(String inputDate) {
        final var date = convertToDate(inputDate);
        return convertToString(getFirstWorkingDay(date));
    }

    public static boolean isBefore(String date1, String date2) {
        return convertToDate(date1).isBefore(convertToDate(date2));
    }

}
