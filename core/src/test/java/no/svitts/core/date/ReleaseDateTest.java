package no.svitts.core.date;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReleaseDateTest {

    @Test
    public void fromString_ShouldReturnReleaseDateWithExpectedTime() {
        String string = new DateTime(2016, 1, 1, 0, 0).toString();
        ReleaseDate releaseDate = ReleaseDate.fromString(string);
        assertTrue(releaseDate != null);
        assertEquals(new ReleaseDate(2016, 1, 1).getTime(), releaseDate.getTime());
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
