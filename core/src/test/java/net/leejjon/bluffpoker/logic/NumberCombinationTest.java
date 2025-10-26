package net.leejjon.bluffpoker.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NumberCombinationTest {
    @Test
    public void testCompareToCalls() throws net.leejjon.bluffpoker.logic.InputValidationException {
        NumberCombination call643 = NumberCombination.validNumberCombinationFrom("643");
        NumberCombination call634 = NumberCombination.validNumberCombinationFrom("634");
        NumberCombination throw634 = new NumberCombination(6, 3, 4, true);
        NumberCombination throw643 = new NumberCombination(6, 4, 3, true);

        assertEquals(call643, call643);
        assertNotEquals(call643, call634);
        assertTrue(call643.isGreaterThan(call634));
        assertFalse(call643.isGreaterThan(call643));
        assertFalse(call634.isGreaterThan(call643));
        assertFalse(call643.isGreaterThan(throw634));
        assertFalse(throw643.isGreaterThan(throw634));

        assertThrows(InputValidationException.class,
                () -> NumberCombination.validNumberCombinationFrom("abc"),
                "InputValidationException should have been thrown."
        );
    }

    @Test
    public void testValidateNumberCombinationInput() {
        assertTrue(NumberCombination.validateNumberCombinationInput("643"));
        assertFalse(NumberCombination.validateNumberCombinationInput("781"));
        assertFalse(NumberCombination.validateNumberCombinationInput("abc"));
    }

    @Test
    public void testValidateNumberCombinationConstructor() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new NumberCombination(7, 1, 1, false),
                "Can't have anything higher than 6."
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new NumberCombination(-1, 1, 1, false),
                "Can't have numbers lower than 0."
        );
    }

    @Test
    public void testIncrementing() {
        NumberCombination call643 = new NumberCombination(6, 4, 3, true);

        NumberCombination incrementTo644 = call643.increment();
        assertEquals(new NumberCombination(6, 4, 4, true), incrementTo644);

        NumberCombination incrementTo661 = new NumberCombination(6, 5, 5, true).increment();
        assertEquals(new NumberCombination(6, 6, 1, true), incrementTo661);
    }
}
