package co.opensi.kkiapay;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void isCorrectPhone() throws Exception {
        assertTrue( UtilsKt.is_valid_phone("22967434270"));
        assertTrue( UtilsKt.is_valid_phone("228674342701"));
        assertFalse( UtilsKt.is_valid_phone("2967434270"));
        assertFalse( UtilsKt.is_valid_phone("12967434270"));

    }


}