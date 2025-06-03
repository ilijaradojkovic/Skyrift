package bees.io.Berzza.util;

import java.time.*;

public class DateUtil {

    public static LocalDateTime endDay(LocalDateTime date) {
        return date.withHour(23).withMinute(59).withSecond(59);
    }

    public static LocalDateTime startDay(LocalDateTime date) {
        return date.withHour(0).withMinute(0).withSecond(0);
    }

    public static LocalDateTime endDayToday() {
        return LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
    }

    public static LocalDateTime startDayToday() {
        return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
    }

    public static LocalDateTime startOfMonth() {
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);
        return firstDayOfMonth.atStartOfDay();
    }

    public static LocalDateTime endOfMonth() {
        LocalDate now = LocalDate.now();
        YearMonth yearMonth = YearMonth.from(now);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
        return lastDayOfMonth.atTime(LocalTime.MAX);
    }

    public static LocalDateTime startOfYear() {
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfYear = now.withDayOfYear(1);
        return firstDayOfYear.atStartOfDay();
    }

    public static LocalDateTime endOfYear() {
        LocalDate now = LocalDate.now();
        Year currentYear = Year.from(now);
        LocalDate lastDayOfYear = currentYear.atMonth(12).atEndOfMonth();
        return lastDayOfYear.atTime(LocalTime.MAX);
    }
}
