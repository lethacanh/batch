package batch;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tset {
    @Test
    public void testLong() throws Exception {
        String str = "000123";
        Long aLong = Long.valueOf(str);
        assertEquals(123, aLong);
    }
}
