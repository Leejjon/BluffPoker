package net.leejjon.bluffpoker.logic;

import java.util.Objects;

public class Call {
    public Call(String playerName, NumberCombination numberCombination) {
        this.playerName = playerName;
        this.numberCombination = numberCombination;
    }

    /**
     * Player who made the call.
     */
    private final String playerName;

    /**
     * The NumberCombination that belongs to it.
     */
    private final NumberCombination numberCombination;

    public String getPlayerName() {
        return playerName;
    }

    public NumberCombination getNumberCombination() {
        return numberCombination;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Call call = (Call) o;
        return Objects.equals(playerName, call.playerName) && Objects.equals(numberCombination, call.numberCombination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, numberCombination);
    }
}
