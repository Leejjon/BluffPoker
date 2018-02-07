package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.SnapshotArray;

import net.leejjon.bluffpoker.actors.CupActor;
import net.leejjon.bluffpoker.actors.DiceActor;
import net.leejjon.bluffpoker.logic.Player;
import net.leejjon.bluffpoker.ui.ClickableLabel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public enum GameStateEnum implements GameStateAssertor, UserInterfaceAssertor {
    NEW_GAME {
        @Override
        public void assertDices(GameState gameState) {
            assertEquals(6, gameState.getLeftDice().getDiceValue());
            assertEquals(4, gameState.getMiddleDice().getDiceValue());
            assertEquals(3, gameState.getRightDice().getDiceValue());
        }

        @Override
        public void assertCallBoard(GameState gameState) {
            assertEquals("000", gameState.getCallInput());
            assertEquals(false, gameState.isAllowedToCall());
        }

        @Override
        public void assertCup(Cup cup) {
            assertFalse(cup.isBelieving());
            assertFalse(cup.isLocked());
            assertFalse(cup.isWatchingOwnThrow());
        }

        @Override
        public void assertPlayers(GameState gameState) {
            Player[] players = gameState.getPlayers();
            Player leon = players[0];
            assertEquals("Leon", leon.getName());
            assertEquals(3, leon.getLives());

            Player dirk = players[1];
            assertEquals("Dirk", dirk.getName());
            assertEquals(3, dirk.getLives());

            Player currentPlayer = gameState.getCurrentPlayer();
            assertEquals(leon, currentPlayer);
        }

        @Override
        public void assertStatusses(GameState gameState) {
            assertNull(gameState.getLatestCall());
        }

        @Override
        public void assertCallBoard(Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
            assertEquals("000", callInputLabel.getText().toString());
            assertTrue(autoButton.isDisabled());
            assertTrue(callButton.isDisabled());
        }

        @Override
        public void assertCup(CupActor cupActor) {
            assertTrue(cupActor.isVisible());
            assertEquals(cupActor.getClosedCupDrawable(), cupActor.getCupImage().getDrawable());
        }

        @Override
        public void assertDices(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors) {
            // Default is 643, but -1 because of array index will make it 532.
            Drawable drawableOfDiceImage6 = left.getSpriteDrawables()[5];
            assertEquals(drawableOfDiceImage6, left.getDiceImage().getDrawable());

            Drawable drawableOfDiceImage4 = middle.getSpriteDrawables()[3];
            assertEquals(drawableOfDiceImage4, middle.getDiceImage().getDrawable());

            Drawable drawableOfDiceImage3 = right.getSpriteDrawables()[2];
            assertEquals(drawableOfDiceImage3, right.getDiceImage().getDrawable());

            assertIfDicesAreInUnderCupGroup(left, middle, right, dicesUnderCupActors);
        }
    };

    void assertIfDicesAreInUnderCupGroup(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors) {
        // Assert if the dices have been added to the correct group.
        SnapshotArray<Actor> children = dicesUnderCupActors.getChildren();
        assertEquals(left, children.get(0));
        assertEquals(middle, children.get(1));
        assertEquals(right, children.get(2));
    }
}
