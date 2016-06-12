package no.svitts.core.date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReleaseDateTest {

    @Test
    public void toSqlDate_ShouldReturnSqlDateWithExpectedTime() {
        ReleaseDate releaseDate = new ReleaseDate(2016, 1, 1);
        java.sql.Date date = releaseDate.toSqlDate();
        assertTrue(date != null);
        assertEquals(date.getTime(), releaseDate.getTime());
    }

}
