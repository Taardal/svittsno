package no.svitts.core.date;

import no.svitts.core.exception.KeyDateParseException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class KeyDate {

    private static final String KEY_DATE_REGEX = "(\\d{4})(\\d{2})(\\d{2})";
    private static final String JAVA_SQL_DATE_REGEX = "(\\d{4})-(\\d{2})-(\\d{2})";
    private static final DateTimeFormatter KEY_DATE_PATTERN = DateTimeFormat.forPattern("yyyyMMdd");
    private static final DateTimeFormatter JAVA_SQL_DATE_PATTERN = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final DateTimeFormatter PRETTY_PATTERN = DateTimeFormat.forPattern("dd.MMMM.yyyy");
    private DateTime dateTime;

    public KeyDate() {
        dateTime = DateTime.now();
    }

    public KeyDate(int year, int monthOfYear, int dayOfMonth) {
        dateTime = new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
    }

    public KeyDate(String date) {
        dateTime = parseDateTime(date);
    }

    public KeyDate(java.sql.Date date) {
        dateTime = new DateTime(date);
    }

    public KeyDate(java.util.Date date) {
        dateTime = new DateTime(date);
    }

    public java.sql.Date toJavaSqlDate() {
        return new java.sql.Date(dateTime.toInstant().getMillis());
    }

    public java.util.Date toJavaUtilDate() {
        return new java.util.Date(dateTime.toInstant().getMillis());
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

    @Override
    public String toString() {
        return PRETTY_PATTERN.print(getTime());
    }

    private DateTime parseDateTime(String date) {
        if (date.matches(KEY_DATE_REGEX)) {
            return KEY_DATE_PATTERN.parseDateTime(date);
        } else if (date.matches(JAVA_SQL_DATE_REGEX)) {
            return JAVA_SQL_DATE_PATTERN.parseDateTime(date);
        } else {
            throw new KeyDateParseException("Could not parse string [" + date + "] to DateTime");
        }
    }

}
