package no.svitts.core.date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ReleaseDateTest {

    @Test
    public void toSqlDate_ShouldReturnSqlDateWithExpectedTime() {
        ReleaseDate releaseDate = new ReleaseDate(2016, 1, 1);
        java.sql.Date sqlDate = releaseDate.toSqlDate();
        assertTrue(sqlDate != null);
        assertEquals(sqlDate.getTime(), releaseDate.getTime());
    }

    @Test
    public void fromString_ReleaseDateFormat_ShouldReturnReleaseDateWithExpectedTime() {
        ReleaseDate releaseDate = new ReleaseDate(2016, 1, 1);
        ReleaseDate releaseDateFromString = ReleaseDate.fromString("20160101");
        assertTrue(releaseDateFromString != null);
        assertEquals(releaseDate.getTime(), releaseDateFromString.getTime());
    }

    @Test
    public void fromString_SqlFormat_ShouldReturnReleaseDateWithExpectedTime() {
        ReleaseDate releaseDate = new ReleaseDate(2016, 1, 1);
        ReleaseDate releaseDateFromString = ReleaseDate.fromString("2016-01-01");
        assertTrue(releaseDateFromString != null);
        assertEquals(releaseDate.getTime(), releaseDateFromString.getTime());
    }

    @Test
    public void fromString_InvalidFormat_ShouldReturnNull() {
        assertNull(ReleaseDate.fromString("01.01.2016"));
    }

    @Test
    public void fromString_StringIsNull_ShouldReturnNull() {
        assertNull(ReleaseDate.fromString(null));
    }

    @Test
    public void fromString_StringIsEmpty_ShouldReturnNull() {
        assertNull(ReleaseDate.fromString(""));
    }

}
