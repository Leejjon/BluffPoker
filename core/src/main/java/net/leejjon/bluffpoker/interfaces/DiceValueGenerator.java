package net.leejjon.bluffpoker.interfaces;

import java.util.Random;

/**
 * This logic used to be in the dice class. But to make the outcome of the dice values predictable
 */
public interface DiceValueGenerator {
    default int generateRandomDiceValue() {
        Random randomDiceNumber = new Random();
        return randomDiceNumber.nextInt(6);
    }
}
