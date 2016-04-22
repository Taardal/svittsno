package no.svitts.core.date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class KeyDate {

    private static final DateTimeFormatter PRETTY_PATTERN = DateTimeFormat.forPattern("dd.MMMM.yyyy");
    private DateTime dateTime;

    public KeyDate() {
        dateTime = DateTime.now();
    }

    public KeyDate(int year, int monthOfYear, int dayOfMonth) {
        dateTime = new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
    }

    public KeyDate(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public KeyDate(java.sql.Date date) {
        dateTime = new DateTime(date);
    }

    public java.sql.Date toSqlDate() {
        return new java.sql.Date(dateTime.toInstant().getMillis());
    }

    public long getTime() {
        return dateTime.toInstant().getMillis();
    }

    public boolean isBefore(KeyDate keyDate) {
        return dateTime.isBefore(keyDate.getTime());
    }

    public boolean isAfter(KeyDate keyDate) {
        return dateTime.isAfter(keyDate.getTime());
    }

    public KeyDate plusHours(int hours) {
        return new KeyDate(dateTime.plusHours(hours));
    }

    public KeyDate plusMinutes(int minutes) {
        return new KeyDate(dateTime.plusMinutes(minutes));
    }

    public KeyDate plusSeconds(int seconds) {
        return new KeyDate(dateTime.plusSeconds(seconds));
    }

    public KeyDate startOfDay() {
        return new KeyDate(dateTime
                .hourOfDay().withMinimumValue()
                .minuteOfHour().withMinimumValue()
                .secondOfMinute().withMinimumValue());
    }

    public KeyDate endOfDay() {
        return new KeyDate(dateTime
                .hourOfDay().withMaximumValue()
                .minuteOfHour().withMaximumValue()
                .secondOfMinute().withMaximumValue());
    }

    @Override
    public String toString() {
        return PRETTY_PATTERN.print(getTime());
    }

}
