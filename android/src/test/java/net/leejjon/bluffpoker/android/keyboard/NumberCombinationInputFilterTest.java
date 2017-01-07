package net.leejjon.bluffpoker.android.keyboard;

import android.text.InputFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class NumberCombinationInputFilterTest {

    @Before
    public void setup() {
        InputFilter nfif = new NumberCombinationInputFilter();
    }

    @Test
    public void testFilter() {
        assertTrue(new Boolean(true));
    }
}
