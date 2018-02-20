package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.SnapshotArray;

import net.leejjon.bluffpoker.actors.CupActor;
import net.leejjon.bluffpoker.actors.DiceActor;
import net.leejjon.bluffpoker.ui.ClickableLabel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public enum GameStateEnum implements GameStateAssertor, UserInterfaceAssertor {
    NEW_GAME {
        @Override
        public void assertCallBoard() {
            assertEquals("000", GameState.state().getCallInput());
            assertEquals(false, GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCup() {
            assertFalse(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
            assertFalse(GameState.state().getCup().isWatchingOwnThrow());
        }

        @Override
        public void assertStatusses() {
            assertTrue(GameState.state().isAllowedToThrow());
            assertFalse(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertFalse(GameState.state().isAllowedToViewOwnThrow());
            assertNull(GameState.state().getLatestCall());
        }

        @Override
        public void assertCallBoard(Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
            assertEquals("000", callInputLabel.getText().toString());
            assertTrue(autoButton.isDisabled());
            assertTrue(callButton.isDisabled());
        }

        @Override
        public void assertCup(CupActor cupActor) {
            assertEquals(cupActor.getClosedCupDrawable(), cupActor.getCupImage().getDrawable());
            assertFalse(cupActor.getLockImage().isVisible());
        }
    },
    AFTER_FIRST_SHAKE {
        @Override
        public void assertCallBoard() {
            assertEquals("000", GameState.state().getCallInput());
            assertEquals(true, GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCup() {
            assertFalse(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
            assertFalse(GameState.state().getCup().isWatchingOwnThrow());
        }

        @Override
        public void assertStatusses() {
            assertFalse(GameState.state().isAllowedToThrow());
            assertFalse(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertTrue(GameState.state().isAllowedToViewOwnThrow());
            assertNull(GameState.state().getLatestCall());
        }

        @Override
        public void assertCallBoard(Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
            assertEquals("000", callInputLabel.getText().toString());
            assertFalse(autoButton.isDisabled());
            assertFalse(callButton.isDisabled());
        }

        @Override
        public void assertCup(CupActor cupActor) {
            assertEquals(cupActor.getClosedCupDrawable(), cupActor.getCupImage().getDrawable());
            assertFalse(cupActor.getLockImage().isVisible());
        }
    },
    VIEW_AFTER_FIRST_SHAKE {
        @Override
        public void assertCallBoard(Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
            assertEquals("000", GameState.state().getCallInput());
            assertEquals(true, GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCup(CupActor cupActor) {
            assertEquals(cupActor.getOpenCupDrawable(), cupActor.getCupImage().getDrawable());
            assertFalse(cupActor.getLockImage().isVisible());
        }

        @Override
        public void assertCallBoard() {
            assertEquals("000", GameState.state().getCallInput());
            assertEquals(true, GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCup() {
            assertFalse(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
            assertTrue(GameState.state().getCup().isWatchingOwnThrow());
        }

        @Override
        public void assertStatusses() {
            assertFalse(GameState.state().isAllowedToThrow());
            assertFalse(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertTrue(GameState.state().isAllowedToViewOwnThrow());
            assertNull(GameState.state().getLatestCall());
        }
    };
}
