package net.leejjon.bluffpoker.state;

import net.leejjon.bluffpoker.logic.BluffPokerGame;
import net.leejjon.bluffpoker.logic.NumberCombination;
import net.leejjon.bluffpoker.logic.Player;

import static org.junit.Assert.assertEquals;

public interface GameStateAssertor {
    default void assertDices(NumberCombination expectedNumberCombination) {
        assertEquals(expectedNumberCombination.getHighestNumber(), GameState.state().getLeftDice().getDiceValue());
        assertEquals(expectedNumberCombination.getMiddleNumber(), GameState.state().getMiddleDice().getDiceValue());
        assertEquals(expectedNumberCombination.getLowestNumber(), GameState.state().getRightDice().getDiceValue());
    }

    void assertCallBoard();

    void assertCup();

    default void assertPlayers() {
        Player[] players = GameState.state().getPlayers();
        Player leon = players[0];
        assertEquals("Leon", leon.getName());
        assertEquals(3, leon.getLives());

        Player dirk = players[1];
        assertEquals("Dirk", dirk.getName());
        assertEquals(3, dirk.getLives());

        Player currentPlayer = GameState.state().getCurrentPlayer();
        assertEquals(leon, currentPlayer);
    }

    void assertStatusses();

    default void assertState(BluffPokerGame game, NumberCombination expectedNumberCombination) {
        assertPlayers();
        assertDices(expectedNumberCombination);
        assertCallBoard();
        assertCup();
        assertStatusses();
        assertEquals(expectedNumberCombination, game.getNumberCombinationFromDices());
    }
}
