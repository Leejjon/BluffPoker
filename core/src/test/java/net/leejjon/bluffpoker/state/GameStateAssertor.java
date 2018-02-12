package net.leejjon.bluffpoker.state;

import net.leejjon.bluffpoker.logic.BluffPokerGame;
import net.leejjon.bluffpoker.logic.NumberCombination;

import static org.junit.Assert.assertEquals;

public interface GameStateAssertor {
    default void assertDices(GameState gameState, NumberCombination expectedNumberCombination) {
        assertEquals(expectedNumberCombination.getHighestNumber(), gameState.getLeftDice().getDiceValue());
        assertEquals(expectedNumberCombination.getMiddleNumber(), gameState.getMiddleDice().getDiceValue());
        assertEquals(expectedNumberCombination.getLowestNumber(), gameState.getRightDice().getDiceValue());
    }

    void assertCallBoard(GameState gameState);

    void assertCup(Cup cup);

    void assertPlayers(GameState gameState);

    void assertStatusses(GameState gameState);

    default void assertGameState(GameState gameState, NumberCombination expectedNumberCombination) {
        assertPlayers(gameState);
        assertDices(gameState, expectedNumberCombination);
        assertCallBoard(gameState);
        assertCup(gameState.getCup());
        assertStatusses(gameState);
    }

    default void assertBluffPokerGame(BluffPokerGame game, NumberCombination expectedNumberCombination) {
        assertEquals(expectedNumberCombination, game.getNumberCombinationFromDices());
    }
}
