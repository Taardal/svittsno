package no.svitts.core.date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class KeyDate {

    private static final String KEY_DATE_REGEX = "((19|20)(\\d)(\\d))(0?[1-9]|1[012])(0?[1-9]|[12][0-9]|3[01])";
    private static final String SQL_DATE_REGEX = "((19|20)(\\d)(\\d))(-)(0?[1-9]|1[012])(-)(0?[1-9]|[12][0-9]|3[01])";
    private static final DateTimeFormatter KEY_DATE_PATTERN = DateTimeFormat.forPattern("yyyyMMdd");
    private static final DateTimeFormatter SQL_DATE_PATTERN = DateTimeFormat.forPattern("yyyy-MM-dd");

    private DateTime dateTime;

    public KeyDate() {
        dateTime = DateTime.now();
    }

    public KeyDate(String date) {
        dateTime = parseDateTime(date);
    }

    public KeyDate(int year, int monthOfYear, int dayOfMonth) {
        dateTime = new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
    }

    public KeyDate(java.sql.Date date) {
        dateTime = new DateTime(date);
    }

    private KeyDate(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return KEY_DATE_PATTERN.print(getTime());
    }

    @Override
    public boolean equals(Object object) {
        if (getClass() == object.getClass()) {
            KeyDate keyDate = (KeyDate) object;
            return getTime() == keyDate.getTime();
        } else {
            return false;
        }
    }

    public java.sql.Date toSqlDate() {
        return new java.sql.Date(getTime());
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

    private DateTime parseDateTime(String date) {
        if (date.matches(KEY_DATE_REGEX)) {
            return KEY_DATE_PATTERN.parseDateTime(date);
        } else if (date.matches(SQL_DATE_REGEX)) {
            return SQL_DATE_PATTERN.parseDateTime(date);
        } else {
            throw new RuntimeException("Could not parse date [" + date + "]. Did not match any regex.");
        }
    }

}
