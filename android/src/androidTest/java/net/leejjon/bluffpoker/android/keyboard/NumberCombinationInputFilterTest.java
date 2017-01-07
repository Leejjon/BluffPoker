package net.leejjon.bluffpoker.android.keyboard;

import android.support.test.runner.AndroidJUnit4;
import android.text.SpannedString;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class NumberCombinationInputFilterTest {
    @Test
    public void testFilter() {
        NumberCombinationInputFilter ncif = new NumberCombinationInputFilter();

        // Test with a valid value.
        String typedOrPastedValue = "1";
        SpannedString spannedString = new SpannedString("");
        CharSequence overwrittenValue = ncif.filter(typedOrPastedValue, 0, 1, spannedString, 0, 0);
        // We don't expect to overwrite anything.
        assertNull(overwrittenValue);

        // Test with an invalid value.
        typedOrPastedValue = "a";
        overwrittenValue = ncif.filter(typedOrPastedValue, 0, 1, spannedString, 0, 0);
        assertEquals("", overwrittenValue);

        // Test with a value where the range is too long.
        typedOrPastedValue = "6666";
        overwrittenValue = ncif.filter(typedOrPastedValue, 0, 4, spannedString, 0, 0);
        assertEquals("666", overwrittenValue);
    }
}
