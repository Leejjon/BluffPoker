package net.leejjon.bluffpoker.state;

import net.leejjon.bluffpoker.logic.BluffPokerGame;
import net.leejjon.bluffpoker.logic.InputValidationException;
import net.leejjon.bluffpoker.logic.NumberCombination;
import net.leejjon.bluffpoker.logic.Player;

import lombok.SneakyThrows;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public interface GameStateAssertor {
    default void assertDices(NumberCombination expectedNumberCombination) {
        assertExpectedNumberCombinationWithGameState(expectedNumberCombination);
        assertTrue(GameState.state().getLeftDice().isUnderCup());
        assertTrue(GameState.state().getMiddleDice().isUnderCup());
        assertTrue(GameState.state().getRightDice().isUnderCup());

        assertFalse(GameState.state().getLeftDice().isLocked());
        assertFalse(GameState.state().getMiddleDice().isLocked());
        assertFalse(GameState.state().getRightDice().isLocked());
    }

    default void assertExpectedNumberCombinationWithGameState(NumberCombination expectedNumberCombination) {
        assertEquals(expectedNumberCombination.getHighestNumber(), GameState.state().getLeftDice().getDiceValue());
        assertEquals(expectedNumberCombination.getMiddleNumber(), GameState.state().getMiddleDice().getDiceValue());
        assertEquals(expectedNumberCombination.getLowestNumber(), GameState.state().getRightDice().getDiceValue());
    }

    void assertCallBoardInGameState(String expectedCall);

    void assertCupInGameState();

    default void assertPlayersInGameState() {
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

    void assertStatusses(NumberCombination expectedCall);

    default void assertState(BluffPokerGame game, NumberCombination expectedNumberCombination, NumberCombination expectedCall) {
        assertPlayersInGameState();
        assertDices(expectedNumberCombination);
        assertCallBoardInGameState(expectedCall != null ? expectedCall.toString() : "000");
        assertCupInGameState();
        assertStatusses(expectedCall);
        assertEquals(expectedNumberCombination, game.getNumberCombinationFromDices());
    }
}
