package net.leejjon.bluffpoker;

/**
 * Created by Leejjon on 12-10-2015.
 */
public class NumberCombination {
    private int highestNumber = 0;
    private int middleNumber = 0;
    private int lowestNumber = 0;
    private boolean call;

    public NumberCombination(int firstNumber, int secondNumber, int thirdNumber, boolean call) {
        this.call = call;

        // If it's a validateCall, take the number exactly how it's being inserted.
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

    public boolean isGreaterThan(NumberCombination numberCombinationToCompare) {
        if (highestNumber > numberCombinationToCompare.getHighestNumber()) {
            return true;
        } else if (highestNumber < numberCombinationToCompare.getHighestNumber()) {
            return false;
        } else /* highestNumber == numberCombinationToCompare.getHighestNumber() */ {
            if (middleNumber > numberCombinationToCompare.getMiddleNumber()) {
                return true;
            } else if (middleNumber < numberCombinationToCompare.getMiddleNumber()){
                return false;
            } else /* middleNumber == numberCombinationToCompare.getMiddleNumber() */ {
                if (lowestNumber > numberCombinationToCompare.getLowestNumber()) {
                    return true;
                } else /* lowestNumber <= numberCombinationToCompare.getLowestNumber() */ {
                    return false;
                }
            }
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
        if (numberCombination.getHighestNumber() == highestNumber &&
                numberCombination.getMiddleNumber() == middleNumber &&
                numberCombination.getLowestNumber() == lowestNumber) {
            return true;
        } else {
            return false;
        }
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
}
