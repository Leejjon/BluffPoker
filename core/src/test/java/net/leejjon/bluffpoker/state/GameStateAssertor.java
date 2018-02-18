package net.leejjon.bluffpoker.state;

import net.leejjon.bluffpoker.logic.BluffPokerGame;
import net.leejjon.bluffpoker.logic.NumberCombination;
import net.leejjon.bluffpoker.logic.Player;

import static org.junit.Assert.assertEquals;

public interface GameStateAssertor {
    default void assertDices(GameState gameState, NumberCombination expectedNumberCombination) {
        assertEquals(expectedNumberCombination.getHighestNumber(), gameState.getLeftDice().getDiceValue());
        assertEquals(expectedNumberCombination.getMiddleNumber(), gameState.getMiddleDice().getDiceValue());
        assertEquals(expectedNumberCombination.getLowestNumber(), gameState.getRightDice().getDiceValue());
    }

    void assertCallBoard(GameState gameState);

    void assertCup(Cup cup);

    default void assertPlayers() {
        Player[] players = GameState.get().getPlayers();
        Player leon = players[0];
        assertEquals("Leon", leon.getName());
        assertEquals(3, leon.getLives());

        Player dirk = players[1];
        assertEquals("Dirk", dirk.getName());
        assertEquals(3, dirk.getLives());

        Player currentPlayer = GameState.get().getCurrentPlayer();
        assertEquals(leon, currentPlayer);
    }

    void assertStatusses(GameState gameState);

    default void assertGameState(GameState gameState, NumberCombination expectedNumberCombination) {
        assertPlayers();
        assertDices(gameState, expectedNumberCombination);
        assertCallBoard(gameState);
        assertCup(gameState.getCup());
        assertStatusses(gameState);
    }

    default void assertBluffPokerGame(BluffPokerGame game, NumberCombination expectedNumberCombination) {
        assertEquals(expectedNumberCombination, game.getNumberCombinationFromDices());
    }
}
