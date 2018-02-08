package net.leejjon.bluffpoker.state;

import net.leejjon.bluffpoker.logic.NumberCombination;

import static org.junit.Assert.assertEquals;

public interface GameStateAssertor {
    default void assertDices(GameState gameState) {
        assertEquals(getExpectedDiceNumber().getHighestNumber(), gameState.getLeftDice().getDiceValue());
        assertEquals(getExpectedDiceNumber().getMiddleNumber(), gameState.getMiddleDice().getDiceValue());
        assertEquals(getExpectedDiceNumber().getLowestNumber(), gameState.getRightDice().getDiceValue());
    }

    void assertCallBoard(GameState gameState);

    void assertCup(Cup cup);

    void assertPlayers(GameState gameState);

    void assertStatusses(GameState gameState);

    NumberCombination getExpectedDiceNumber();

    default void assertGameState(GameState gameState) {
        assertPlayers(gameState);
        assertDices(gameState);
        assertCallBoard(gameState);
        assertCup(gameState.getCup());
        assertStatusses(gameState);
    }
}
