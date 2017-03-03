package net.leejjon.bluffpoker.android.keyboard;

import android.support.test.runner.AndroidJUnit4;
import android.text.SpannedString;
import org.junit.Test;
import org.junit.runner.RunWith;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class PlayerNameInputFilterTest {
    @Test
    public void testFilter() {
        PlayerNameInputFilter pnif = new PlayerNameInputFilter();

        // Test with a valid value.
        String typedOrPastedValue = "Leon";
        SpannedString spannedString = new SpannedString("");
        CharSequence overwrittenValue = pnif.filter(typedOrPastedValue, 0, typedOrPastedValue.length(), spannedString, 0, 0);
        // We don't expect to overwrite anything.
        assertNull(overwrittenValue);

        // Test with a letter that to be replaced by a regular ascii value.
        typedOrPastedValue = "ö";
        overwrittenValue = pnif.filter(typedOrPastedValue, 0, 1, spannedString, 0, 0);
        assertEquals("", overwrittenValue);

        // Test with a value where the range is too long.
        typedOrPastedValue = "1234567890abcdef1234";
        overwrittenValue = pnif.filter(typedOrPastedValue, 0, typedOrPastedValue.length(), spannedString, 0, 0);
        assertEquals("1234567890abcdef", overwrittenValue);

        // Test with a space in the middle of the name (should be allowed).
        typedOrPastedValue = "Darth Vader";
        overwrittenValue = pnif.filter(typedOrPastedValue, 0, typedOrPastedValue.length(), spannedString, 0, 0);
        assertNull(overwrittenValue);
    }
}
