package net.leejjon.bluffpoker;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumberCombinationTest {
    @Test
    public void testCompareToCalls() throws InputValidationException {
        NumberCombination call643 = NumberCombination.validNumberCombinationFrom("643");
        NumberCombination call634 = NumberCombination.validNumberCombinationFrom("634");
        NumberCombination throw634 = new NumberCombination(6, 3, 4, true);
        NumberCombination throw643 = new NumberCombination(6, 4, 3, true);

        assertTrue(call643.equals(call643));
        assertFalse(call643.equals(call634));
        assertTrue(call643.isGreaterThan(call634));
        assertFalse(call643.isGreaterThan(call643));
        assertFalse(call634.isGreaterThan(call643));
        assertFalse(call643.isGreaterThan(throw634));
        assertFalse(throw643.isGreaterThan(throw634));

        try {
            NumberCombination callAbc = NumberCombination.validNumberCombinationFrom("abc");
            fail("InputValidationException should have been thrown.");
        } catch (InputValidationException e) {

        }
    }

    @Test
    public void testValidateNumberCombinationInput() {
        assertTrue(NumberCombination.validateNumberCombinationInput("643"));
        assertFalse(NumberCombination.validateNumberCombinationInput("781"));
        assertFalse(NumberCombination.validateNumberCombinationInput("abc"));
    }
}
