package net.leejjon.bluffpoker.state;

import net.leejjon.bluffpoker.logic.Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public enum GameStateAssertor implements AssertableGameState {
    NEW_GAME {
        @Override
        public void assertDices(GameState gameState) {
            assertEquals(6, gameState.getLeftDice().getDiceValue());
            assertEquals(4, gameState.getMiddleDice().getDiceValue());
            assertEquals(3, gameState.getRightDice().getDiceValue());
        }

        @Override
        public void assertPlayers(GameState gameState) {
            Player currentPlayer = gameState.getCurrentPlayer();
            assertNotNull(currentPlayer);
            assertEquals("Leon", currentPlayer.getName());
            assertEquals(3, currentPlayer.getLives());
        }
    }
}
