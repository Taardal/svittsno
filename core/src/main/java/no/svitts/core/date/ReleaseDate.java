package no.svitts.core.date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;

public class ReleaseDate {

    private static final String KEY_DATE_REGEX = "((19|20)(\\d)(\\d))(0?[1-9]|1[012])(0?[1-9]|[12][0-9]|3[01])";
    private static final String SQL_DATE_REGEX = "((19|20)(\\d)(\\d))(-)(0?[1-9]|1[012])(-)(0?[1-9]|[12][0-9]|3[01])";
    private static final DateTimeFormatter KEY_DATE_PATTERN = DateTimeFormat.forPattern("yyyyMMdd");
    private static final DateTimeFormatter SQL_DATE_PATTERN = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final Logger LOGGER = LoggerFactory.getLogger(ReleaseDate.class);

    private DateTime dateTime;

    public ReleaseDate() {
        dateTime = DateTime.now();
    }

    public ReleaseDate(int year, int monthOfYear, int dayOfMonth) {
        dateTime = new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
    }

    public ReleaseDate(java.sql.Date date) {
        dateTime = new DateTime(date);
    }

    private ReleaseDate(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public static ReleaseDate fromString(String date) {
        return date != null && !date.equals("") ? new ReleaseDate(parseDateTime(date)) : null;
    }

    @Override
    public String toString() {
        return KEY_DATE_PATTERN.print(getTime());
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof ReleaseDate) {
            ReleaseDate releaseDate = (ReleaseDate) object;
            return getTime() == releaseDate.getTime();
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

    public boolean isBefore(ReleaseDate releaseDate) {
        return dateTime.isBefore(releaseDate.getTime());
    }

    public boolean isAfter(ReleaseDate releaseDate) {
        return dateTime.isAfter(releaseDate.getTime());
    }

    public ReleaseDate plusHours(int hours) {
        return new ReleaseDate(dateTime.plusHours(hours));
    }

    public ReleaseDate plusMinutes(int minutes) {
        return new ReleaseDate(dateTime.plusMinutes(minutes));
    }

    public ReleaseDate plusSeconds(int seconds) {
        return new ReleaseDate(dateTime.plusSeconds(seconds));
    }

    public ReleaseDate startOfDay() {
        return new ReleaseDate(dateTime
                .hourOfDay().withMinimumValue()
                .minuteOfHour().withMinimumValue()
                .secondOfMinute().withMinimumValue());
    }

    public ReleaseDate endOfDay() {
        return new ReleaseDate(dateTime
                .hourOfDay().withMaximumValue()
                .minuteOfHour().withMaximumValue()
                .secondOfMinute().withMaximumValue());
    }

    private static DateTime parseDateTime(String date) {
        if (date.matches(KEY_DATE_REGEX)) {
            return KEY_DATE_PATTERN.parseDateTime(date);
        } else if (date.matches(SQL_DATE_REGEX)) {
            return SQL_DATE_PATTERN.parseDateTime(date);
        } else {
            String errorMessage = "Could not parse date [" + date + "] because it did not match any regex.";
            LOGGER.warn(errorMessage);
            throw new InvalidParameterException(errorMessage);
        }
    }

}
