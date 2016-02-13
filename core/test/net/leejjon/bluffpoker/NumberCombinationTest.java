package net.leejjon.bluffpoker;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Leejjon on 25-1-2016.
 */
public class NumberCombinationTest {
    @Test
    public void testCompareCalls() {
        NumberCombination call643 = new NumberCombination(6, 4, 3, true);
        NumberCombination call634 = new NumberCombination(6, 3, 4, true);
        NumberCombination throw634 = new NumberCombination(6, 3, 4, false);
        NumberCombination throw643 = new NumberCombination(6, 4, 3, false);

        Assert.assertEquals(true, call643.isGreaterThan(call634));
        Assert.assertEquals(false, call643.isGreaterThan(call643));
        Assert.assertEquals(false, call634.isGreaterThan(call643));
        Assert.assertEquals(false, call643.isGreaterThan(throw634));
        Assert.assertEquals(false, throw643.isGreaterThan(throw634));
    }
}
