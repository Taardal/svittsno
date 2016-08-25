package no.svitts.core.date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class ReleaseDate {

    private static final DateTimeFormatter RELEASE_DATE_PATTERN = DateTimeFormat.forPattern("yyyyMMdd");
    private static final Logger LOGGER = LoggerFactory.getLogger(ReleaseDate.class);

    @Transient
    private DateTime dateTime;

    public ReleaseDate() {
        dateTime = DateTime.now();
    }

    public ReleaseDate(int year, int monthOfYear, int dayOfMonth) {
        dateTime = new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
    }

    public ReleaseDate(long time) {
        dateTime = new DateTime(time);
    }

    private ReleaseDate(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public static ReleaseDate fromString(String string) {
        if (string != null && !string.equals("")) {
            return new ReleaseDate(DateTime.parse(string));
        } else {
            LOGGER.warn("Could not parse string [" + string + "] to release date because it was null or empty.");
            return null;
        }
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

    @Column(name = "datetime")
    public long getTime() {
        return dateTime.toInstant().getMillis();
    }

    public void setTime(long millis) {
        dateTime = new DateTime(millis);
    }

}
