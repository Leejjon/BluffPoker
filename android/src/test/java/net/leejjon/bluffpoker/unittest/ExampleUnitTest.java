package net.leejjon.bluffpoker.unittest;

import android.text.SpannedString;
import net.leejjon.bluffpoker.android.keyboard.NumberCombinationInputFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    @Mock
    private SpannedString spanned;

    @Test
    public void testMethod() {
        NumberCombinationInputFilter ncif = new NumberCombinationInputFilter();
        assertNotNull(ncif);
    }
}
