package net.leejjon.bluffpoker;

/**
 * Created by Leejjon on 12-10-2015.
 */
public class NumberCombination implements Comparable<NumberCombination> {
    private int highestNumber = 0;
    private int middleNumber = 0;
    private int lowestNumber = 0;
    private boolean call;

    public NumberCombination(int firstNumber, int secondNumber, int thirdNumber, boolean call) {
        this.call = call;

        // If it's a call, take the number exactly how it's being inserted.
        if (call) {
            highestNumber = firstNumber;
            middleNumber = secondNumber;
            lowestNumber = thirdNumber;
        } else { // If it's a throw, 
            int[] numbers = new int[]{firstNumber, secondNumber, thirdNumber};
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
        }
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

    public boolean equals(NumberCombination numberCombination) {
        return compareTo(numberCombination) == 0;
    }

    public boolean areAllDicesEqual() {
        if (highestNumber != 0 && highestNumber == middleNumber && middleNumber == lowestNumber) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "" + highestNumber + middleNumber + lowestNumber;
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
}
