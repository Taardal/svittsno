package no.svitts.core.date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KeyDateTest {

    @Test
    public void toSqlDate_ShouldReturnSqlDateWithExpectedTime() {
        KeyDate keyDate = new KeyDate(2016, 1, 1);
        java.sql.Date date = keyDate.toSqlDate();
        assertTrue(date != null);
        assertEquals(date.getTime(), keyDate.getTime());
    }

}
