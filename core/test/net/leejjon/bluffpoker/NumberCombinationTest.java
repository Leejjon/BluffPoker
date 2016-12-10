package net.leejjon.bluffpoker;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Leejjon on 25-1-2016.
 */
public class NumberCombinationTest {
    @Test
    public void testCompareToCalls() {
        NumberCombination call643 = new NumberCombination(6, 4, 3, true);
        NumberCombination call634 = new NumberCombination(6, 3, 4, true);
        NumberCombination throw634 = new NumberCombination(6, 3, 4, false);
        NumberCombination throw643 = new NumberCombination(6, 4, 3, false);

        assertTrue(call643.equals(call643));
        assertFalse(call643.equals(call634));
        assertTrue(call643.isGreaterThan(call634));
        assertFalse(call643.isGreaterThan(call643));
        assertFalse(call634.isGreaterThan(call643));
        assertFalse(call643.isGreaterThan(throw634));
        assertFalse(throw643.isGreaterThan(throw634));
    }
}
