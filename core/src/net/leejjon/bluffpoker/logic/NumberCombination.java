package net.leejjon.bluffpoker.logic;

import com.google.common.primitives.Ints;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that defines NumberCombinations as used in Bluff poker.
 */
public class NumberCombination implements Comparable<NumberCombination> {
    public static NumberCombination MIN = new NumberCombination(0,0,0, true);
    public static NumberCombination JUNK = new NumberCombination(1, 1, 1, true);
    public static NumberCombination BLUFF_NUMBER = new NumberCombination(6, 4, 3, true);
    public static NumberCombination MAX = new NumberCombination(6, 6, 6, true);

    private final int highestNumber;
    private final int middleNumber;
    private final int lowestNumber;

    /**
     * Use this parameter to generate a NumberCombination object from a throw or hard coded value.
     * @param firstNumber
     * @param secondNumber
     * @param thirdNumber
     * @param ordered
     */
    public NumberCombination(int firstNumber, int secondNumber, int thirdNumber, boolean ordered) {
        // TODO: Add preconditions for the ranges of the integers.

        int[] order;
        if (ordered) {
            order = orderNumbersFromLowToHigh(firstNumber, secondNumber, thirdNumber);
        } else {
            order = new int[] {thirdNumber, secondNumber, firstNumber};
        }

        highestNumber = order[2];
        middleNumber = order[1];
        lowestNumber = order[0];
    }

    private static int[] orderNumbersFromLowToHigh(int highestNumber, int middleNumber, int lowestNumber) {
        List<Integer> listToSort = new ArrayList<>(3);
        listToSort.add(highestNumber);
        listToSort.add(middleNumber);
        listToSort.add(lowestNumber);

        Collections.sort(listToSort);
        return Ints.toArray(listToSort);
    }

    /**
     * Reads easier than compareTo(ncToCompare) > 0.
     */
    public boolean isGreaterThan(NumberCombination ncToCompare) {
        if (compareTo(ncToCompare) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getHighestNumber() {
        return highestNumber;
    }

    public int getMiddleNumber() {
        return middleNumber;
    }

    public int getLowestNumber() {
        return lowestNumber;
    }

    /**
     * Incrementing when a user has believed 666.
     */
    public NumberCombination incrementAll() {
        if (this.equals(NumberCombination.MAX)) {
            return NumberCombination.JUNK;
        } else {
            return new NumberCombination(highestNumber + 1, middleNumber + 1, lowestNumber + 1, false);
        }
    }

    /**
     * Regular increment.
     */
    public NumberCombination increment() {
        // If it is the first throw, incrementing will jump to 643.
        if (this.equals(NumberCombination.MIN)) {
            return NumberCombination.BLUFF_NUMBER;
        } else {
            int highestNumberThatMightIncrement = highestNumber;
            int middleNumberThatMightIncrement = middleNumber;
            int lowestNumberThatMightIncrement = lowestNumber;
            if (lowestNumberThatMightIncrement < middleNumberThatMightIncrement) {
                lowestNumberThatMightIncrement++;
            } else {
                lowestNumberThatMightIncrement = 1;
                if (middleNumberThatMightIncrement < highestNumberThatMightIncrement) {
                    middleNumberThatMightIncrement++;
                } else {
                    middleNumberThatMightIncrement = 1;
                    if (highestNumberThatMightIncrement < 6) {
                        highestNumberThatMightIncrement++;
                    } else { // In case of 666
                        return NumberCombination.JUNK;
                    }
                }
            }
            return new NumberCombination(highestNumberThatMightIncrement, middleNumberThatMightIncrement, lowestNumberThatMightIncrement, true);
        }
    }


    public boolean areAllDicesEqual() {
        if (highestNumber != 0 && highestNumber == middleNumber && middleNumber == lowestNumber) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Use this constructor to generate a NumberCombination object from a call. No ordering is done.
     * @param input A three digit string only containing 0-6
     * @return A valid NumberCombination.
     * @throws InputValidationException
     */
    public static NumberCombination validNumberCombinationFrom(String input) throws InputValidationException {
        if (!validateNumberCombinationInput(input)) {
            throw new InputValidationException("Input did not match expected pattern.");
        }

        // No more validation is done because it already happened with the regex.

        final int decimalRadix = 10;
        return new NumberCombination(Character.digit(input.charAt(0), decimalRadix), Character.digit(input.charAt(1), decimalRadix), Character.digit(input.charAt(2), decimalRadix), false);
    }

    /**
     * Uses Regex to validate the input.
     * @param input A string with a length of 3 that only contains digits in the range of 0-6.
     * @return Returns true if the input is valid, false otherwise.
     */
    protected static boolean validateNumberCombinationInput(String input) {
        // [0-6] means a range from 0-6. The rest is to make sure the length is 3.
        Pattern numberCombinationPattern = Pattern.compile("[0-6][0-6][0-6]");
        Matcher matcher = numberCombinationPattern.matcher(input);
        return matcher.matches();
    }

    @Override
    public boolean equals(Object numberCombination) {
        if (numberCombination == null) return false;
        if (numberCombination == this) return true;
        if (!(numberCombination instanceof NumberCombination))return false;
        return compareTo((NumberCombination) numberCombination) == 0;
    }

    /**
     * @param ncToCompare The NumberCombination object to compare.
     * @return
     * This method returns the value 0 if this NumberCombination is equal to the argument.
     * A value less than 0 if this NumberCombination is less than the argument.
     * A value greater than 0 if this NumberCombination is greater than the argument Integer.
     */
    @Override
    public int compareTo(NumberCombination ncToCompare) {
        if (highestNumber != ncToCompare.getHighestNumber()) {
            return highestNumber - ncToCompare.getHighestNumber();
        } else {
            if (middleNumber != ncToCompare.getMiddleNumber()) {
                return middleNumber - ncToCompare.getMiddleNumber();
            } else {
                return lowestNumber - ncToCompare.getLowestNumber();
            }
        }
    }

    @Override
    public String toString() {
        return "" + highestNumber + middleNumber + lowestNumber;
    }
}
