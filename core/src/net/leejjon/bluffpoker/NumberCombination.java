package net.leejjon.bluffpoker;

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

    private int highestNumber = 0;
    private int middleNumber = 0;
    private int lowestNumber = 0;

    private final boolean ordered;

    /**
     * Use this constructor to generate a NumberCombination object from a call.
     * @param input
     * @throws InputValidationException
     */
    public NumberCombination(String input) throws InputValidationException {
        ordered = false;
        if (!validateNumberCombinationInput(input)) {
            throw new InputValidationException("Input did not match expected pattern.");
        }

        // No more validation is done because it already happened with the regex.

        final int decimalRadix = 10;
        setFinalNumbers(Character.digit(input.charAt(0), decimalRadix), Character.digit(input.charAt(1), decimalRadix), Character.digit(input.charAt(2), decimalRadix));
    }

    /**
     * Use this parameter to generate a NumberCombination object from a throw or hard coded value.
     * @param firstNumber
     * @param secondNumber
     * @param thirdNumber
     * @param ordered
     */
    public NumberCombination(int firstNumber, int secondNumber, int thirdNumber, boolean ordered) {
        this.ordered = ordered;
        // TODO: Add preconditions for the ranges of the integers.

        if (ordered) {
            orderNumbers(firstNumber, secondNumber, thirdNumber);
        } else {
            setFinalNumbers(firstNumber, secondNumber, thirdNumber);
        }
    }

    private void setFinalNumbers(int highestNumber, int middleNumber, int lowestNumber) {
        this.highestNumber = highestNumber;
        this.middleNumber = middleNumber;
        this.lowestNumber = lowestNumber;
    }

    private void orderNumbers(int highestNumber, int middleNumber, int lowestNumber) {
        int[] numbers = new int[]{highestNumber, middleNumber, lowestNumber};
        for (int number : numbers) {
            if (number > highestNumber) {
                lowestNumber = middleNumber;
                middleNumber = highestNumber;
                highestNumber = number;
            } else /* (number <= highestNumber) */ {
                if (number > middleNumber) {
                    lowestNumber = middleNumber;
                    middleNumber = number;
                } else /* number <= middleNumber */ {
                    lowestNumber = number;
                }
            }
        }
        setFinalNumbers(highestNumber, middleNumber, lowestNumber);
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
            if (lowestNumberThatMightIncrement < 6) {
                lowestNumberThatMightIncrement++;
            } else {
                lowestNumberThatMightIncrement = 1;
                if (middleNumberThatMightIncrement < 6) {
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

    public boolean equals(NumberCombination numberCombination) {
        return compareTo(numberCombination) == 0;
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
