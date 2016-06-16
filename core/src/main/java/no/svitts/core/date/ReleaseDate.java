package no.svitts.core.date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReleaseDate {

    private static final String RELEASE_DATE_REGEX = "((19|20)(\\d)(\\d))(0?[1-9]|1[012])(0?[1-9]|[12][0-9]|3[01])";
    private static final String SQL_DATE_REGEX = "((19|20)(\\d)(\\d))(-)(0?[1-9]|1[012])(-)(0?[1-9]|[12][0-9]|3[01])";
    private static final DateTimeFormatter RELEASE_DATE_PATTERN = DateTimeFormat.forPattern("yyyyMMdd");
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
        if (date != null && !date.equals("")) {
            DateTime dateTime = parseDateTime(date);
            if (dateTime != null) {
                return new ReleaseDate(dateTime);
            } else {
                LOGGER.warn("Could not parse date string [" + date + "] to release date because it did not match any supported regex.");
            }
        } else {
            LOGGER.warn("Could not parse date string [" + date + "] to release date because it was null or empty.");
        }
        return null;
    }

    @Override
    public String toString() {
        return RELEASE_DATE_PATTERN.print(getTime());
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

    private static DateTime parseDateTime(String date) {
        if (date.matches(RELEASE_DATE_REGEX)) {
            return RELEASE_DATE_PATTERN.parseDateTime(date);
        } else if (date.matches(SQL_DATE_REGEX)) {
            return SQL_DATE_PATTERN.parseDateTime(date);
        } else {
            return null;
        }
    }

}