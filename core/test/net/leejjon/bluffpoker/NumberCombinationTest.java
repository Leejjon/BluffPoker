package net.leejjon.bluffpoker;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NumberCombinationTest {
    @Test
    public void testCompareToCalls() throws InputValidationException {
        // TODO: Use factory instead of normal constructor
        NumberCombination call643 = new NumberCombination("643");
        NumberCombination call634 = new NumberCombination( "634");
        NumberCombination throw634 = new NumberCombination(6, 3, 4, true);
        NumberCombination throw643 = new NumberCombination(6, 4, 3, true);

        assertTrue(call643.equals(call643));
        assertFalse(call643.equals(call634));
        assertTrue(call643.isGreaterThan(call634));
        assertFalse(call643.isGreaterThan(call643));
        assertFalse(call634.isGreaterThan(call643));
        assertFalse(call643.isGreaterThan(throw634));
        assertFalse(throw643.isGreaterThan(throw634));
    }

    @Test
    public void testValidateNumberCombinationInput() throws InputValidationException {
        assertTrue(NumberCombination.validateNumberCombinationInput("643"));
        assertFalse(NumberCombination.validateNumberCombinationInput("781"));
        assertFalse(NumberCombination.validateNumberCombinationInput("abc"));
    }
}
