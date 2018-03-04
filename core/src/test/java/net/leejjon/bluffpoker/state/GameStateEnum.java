package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.SnapshotArray;

import net.leejjon.bluffpoker.actors.CupActor;
import net.leejjon.bluffpoker.actors.DiceActor;
import net.leejjon.bluffpoker.logic.NumberCombination;
import net.leejjon.bluffpoker.logic.Player;
import net.leejjon.bluffpoker.ui.ClickableLabel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public enum GameStateEnum implements GameStateAssertor, UserInterfaceAssertor {
    NEW_GAME {
        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, GameState.state().getCallInput());
            assertEquals(false, GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
            assertFalse(GameState.state().getCup().isWatchingOwnThrow());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertTrue(GameState.state().isAllowedToThrow());
            assertFalse(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertFalse(GameState.state().isAllowedToViewOwnThrow());
            assertFalse(GameState.state().isBlindPass());
            assertEquals(expectedCall, GameState.state().getLatestCall());
        }
    },
    AFTER_FIRST_SHAKE {
        @Override
        public void assertCallBoardInUI(String expectedCall, Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
            assertEquals(expectedCall, callInputLabel.getText().toString());
            assertFalse(autoButton.isDisabled());
            assertFalse(callButton.isDisabled());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
            assertFalse(GameState.state().getCup().isWatchingOwnThrow());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(GameState.state().isAllowedToThrow());
            assertFalse(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertTrue(GameState.state().isAllowedToViewOwnThrow());
            assertTrue(GameState.state().isBlindPass());
            assertEquals(expectedCall, GameState.state().getLatestCall());
        }

        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, GameState.state().getCallInput());
            assertTrue(GameState.state().isAllowedToCall());
        }
    },
    VIEW_AFTER_FIRST_SHAKE {
        @Override
        public void assertCallBoardInUI(String expectedCall, Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
            assertEquals(expectedCall, callInputLabel.getText().toString());
            assertFalse(autoButton.isDisabled());
            assertFalse(callButton.isDisabled());
        }

        @Override
        public void assertCupInUI(CupActor cupActor) {
            assertEquals(cupActor.getOpenCupDrawable(), cupActor.getCupImage().getDrawable());
            assertFalse(cupActor.getLockImage().isVisible());
        }

        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, GameState.state().getCallInput());
            assertTrue(GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
            assertTrue(GameState.state().getCup().isWatchingOwnThrow());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(GameState.state().isAllowedToThrow());
            assertFalse(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertTrue(GameState.state().isAllowedToViewOwnThrow());
            assertFalse(GameState.state().isBlindPass());
            assertEquals(expectedCall, GameState.state().getLatestCall());
        }
    },
    CALL {
        @Override
        public void assertCallBoardInUI(String expectedCall, Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
            assertEquals(expectedCall, callInputLabel.getText().toString());
            assertTrue(autoButton.isDisabled());
            assertTrue(callButton.isDisabled());
        }

        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, GameState.state().getCallInput());
            assertFalse(GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
            assertFalse(GameState.state().getCup().isWatchingOwnThrow());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(GameState.state().isAllowedToThrow());
            assertTrue(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertFalse(GameState.state().isAllowedToViewOwnThrow());
            assertFalse(GameState.state().isBlindPass());
            assertEquals(expectedCall, GameState.state().getLatestCall().getNumberCombination());
        }
    },
    CALL_BLIND {
        @Override
        public void assertCallBoardInUI(String expectedCall, Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
            assertEquals(expectedCall, callInputLabel.getText().toString());
            assertTrue(autoButton.isDisabled());
            assertTrue(callButton.isDisabled());
        }

        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, GameState.state().getCallInput());
            assertFalse(GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
            assertFalse(GameState.state().getCup().isWatchingOwnThrow());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(GameState.state().isAllowedToThrow());
            assertTrue(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertFalse(GameState.state().isAllowedToViewOwnThrow());
            assertFalse(GameState.state().isBlindPass());
            assertEquals(expectedCall, GameState.state().getLatestCall().getNumberCombination());
        }
    },
    TAKE_6_OUT {
        @Override
        public void assertCallBoardInUI(String expectedCall, Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
            assertEquals(expectedCall, callInputLabel.getText().toString());
            assertFalse(autoButton.isDisabled());
            assertFalse(callButton.isDisabled());
        }

        @Override
        public void assertCupInUI(CupActor cupActor) {
            assertEquals(cupActor.getOpenCupDrawable(), cupActor.getCupImage().getDrawable());
            assertFalse(cupActor.getLockImage().isVisible());
        }

        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, GameState.state().getCallInput());
            assertTrue(GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
            assertTrue(GameState.state().getCup().isWatchingOwnThrow());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(GameState.state().isAllowedToThrow());
            assertFalse(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertTrue(GameState.state().isAllowedToViewOwnThrow());
            assertFalse(GameState.state().isBlindPass());
            assertEquals(expectedCall, GameState.state().getLatestCall());
        }

        @Override
        public void assertDiceLocations(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors, Group dicesBeforeCupActors) {
            // Assert if the dices have been added to the correct group.
            SnapshotArray<Actor> dicesBeforeCup = dicesBeforeCupActors.getChildren();
            assertEquals(left, dicesBeforeCup.get(0));

            SnapshotArray<Actor> dicesUnderCup = dicesUnderCupActors.getChildren();
            assertEquals(middle, dicesUnderCup.get(0));
            assertEquals(right, dicesUnderCup.get(1));
        }

        @Override
        public void assertDices(NumberCombination expectedNumberCombination) {
            assertExpectedNumberCombinationWithGameState(expectedNumberCombination);
            assertFalse(GameState.state().getLeftDice().isUnderCup());
            assertTrue(GameState.state().getMiddleDice().isUnderCup());
            assertTrue(GameState.state().getRightDice().isUnderCup());

            assertFalse(GameState.state().getLeftDice().isLocked());
            assertFalse(GameState.state().getMiddleDice().isLocked());
            assertFalse(GameState.state().getRightDice().isLocked());
        }
    },
    CALL_LEFT_SIX_OUT {
        @Override
        public void assertCallBoardInUI(String expectedCall, Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
            assertEquals(expectedCall, callInputLabel.getText().toString());
            assertTrue(autoButton.isDisabled());
            assertTrue(callButton.isDisabled());
        }

        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, GameState.state().getCallInput());
            assertFalse(GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
            assertFalse(GameState.state().getCup().isWatchingOwnThrow());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(GameState.state().isAllowedToThrow());
            assertTrue(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertFalse(GameState.state().isAllowedToViewOwnThrow());
            assertFalse(GameState.state().isBlindPass());
            assertEquals(expectedCall, GameState.state().getLatestCall().getNumberCombination());
        }

        @Override
        public void assertDiceLocations(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors, Group dicesBeforeCupActors) {
            // Assert if the dices have been added to the correct group.
            SnapshotArray<Actor> dicesBeforeCup = dicesBeforeCupActors.getChildren();
            assertEquals(left, dicesBeforeCup.get(0));

            SnapshotArray<Actor> dicesUnderCup = dicesUnderCupActors.getChildren();
            assertEquals(middle, dicesUnderCup.get(0));
            assertEquals(right, dicesUnderCup.get(1));
        }

        @Override
        public void assertDices(NumberCombination expectedNumberCombination) {
            assertExpectedNumberCombinationWithGameState(expectedNumberCombination);
            assertFalse(GameState.state().getLeftDice().isUnderCup());
            assertTrue(GameState.state().getMiddleDice().isUnderCup());
            assertTrue(GameState.state().getRightDice().isUnderCup());

            assertFalse(GameState.state().getLeftDice().isLocked());
            assertFalse(GameState.state().getMiddleDice().isLocked());
            assertFalse(GameState.state().getRightDice().isLocked());
        }
    },
    BELIEVED_ALL_DICES_UNDER_CUP {
        @Override
        public void assertCupInUI(CupActor cupActor) {
            assertEquals(cupActor.getOpenCupDrawable(), cupActor.getCupImage().getDrawable());
            assertFalse(cupActor.getLockImage().isVisible());
        }

        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, GameState.state().getCallInput());
            assertFalse(GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertTrue(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
            assertFalse(GameState.state().getCup().isWatchingOwnThrow());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(GameState.state().isAllowedToThrow()); // First has to close the cup.
            assertTrue(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertFalse(GameState.state().isAllowedToViewOwnThrow());
            assertFalse(GameState.state().isBlindPass());
            assertEquals(expectedCall, GameState.state().getLatestCall().getNumberCombination());
        }
    },
    BELIEVED_LEFT_SIX_OUT {
        @Override
        public void assertCupInUI(CupActor cupActor) {
            assertEquals(cupActor.getOpenCupDrawable(), cupActor.getCupImage().getDrawable());
            assertFalse(cupActor.getLockImage().isVisible());
        }

        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, GameState.state().getCallInput());
            assertFalse(GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertTrue(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
            assertFalse(GameState.state().getCup().isWatchingOwnThrow());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(GameState.state().isAllowedToThrow()); // First has to close the cup.
            assertTrue(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertFalse(GameState.state().isAllowedToViewOwnThrow());
            assertFalse(GameState.state().isBlindPass());
            assertEquals(expectedCall, GameState.state().getLatestCall().getNumberCombination());
        }

        @Override
        public void assertDiceLocksInUI(DiceActor left, DiceActor middle, DiceActor right) {
            assertTrue(left.getLockImage().isVisible());
            assertFalse(middle.getLockImage().isVisible());
            assertFalse(right.getLockImage().isVisible());
        }

        @Override
        public void assertDiceLocations(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors, Group dicesBeforeCupActors) {
            // Assert if the dices have been added to the correct group.
            SnapshotArray<Actor> dicesBeforeCup = dicesBeforeCupActors.getChildren();
            assertEquals(left, dicesBeforeCup.get(0));

            SnapshotArray<Actor> dicesUnderCup = dicesUnderCupActors.getChildren();
            assertEquals(middle, dicesUnderCup.get(0));
            assertEquals(right, dicesUnderCup.get(1));
        }

        @Override
        public void assertDices(NumberCombination expectedNumberCombination) {
            assertExpectedNumberCombinationWithGameState(expectedNumberCombination);
            assertFalse(GameState.state().getLeftDice().isUnderCup());
            assertTrue(GameState.state().getMiddleDice().isUnderCup());
            assertTrue(GameState.state().getRightDice().isUnderCup());

            assertTrue(GameState.state().getLeftDice().isLocked());
            assertFalse(GameState.state().getMiddleDice().isLocked());
            assertFalse(GameState.state().getRightDice().isLocked());
        }
    },
    NOT_BELIEVE_CORRECT_All_DICES_UNDER_CUP {

        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, GameState.state().getCallInput());
            assertFalse(GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
            assertFalse(GameState.state().getCup().isWatchingOwnThrow());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertTrue(GameState.state().isAllowedToThrow());
            assertFalse(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertFalse(GameState.state().isAllowedToViewOwnThrow());
            assertFalse(GameState.state().isBlindPass());

            if (expectedCall == null) {
                assertNull(GameState.state().getLatestCall());
            } else {
                assertEquals(expectedCall, GameState.state().getLatestCall().getNumberCombination());
            }
        }

        @Override
        public void assertPlayersInGameState(Player expectedCurrentPlayer) {
            Player[] players = GameState.state().getPlayers();
            Player leon = players[0];
            assertEquals("Leon", leon.getName());
            assertEquals(2, leon.getLives());

            Player dirk = players[1];
            assertEquals("Dirk", dirk.getName());
            assertEquals(3, dirk.getLives());

            Player currentPlayer = GameState.state().getCurrentPlayer();
            assertEquals(expectedCurrentPlayer, currentPlayer);
        }
    },
    NOT_BELIEVE_INCORRECT_ALL_DICES_UNDER_CUP {

        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, GameState.state().getCallInput());
            assertFalse(GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
            assertFalse(GameState.state().getCup().isWatchingOwnThrow());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertTrue(GameState.state().isAllowedToThrow());
            assertFalse(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertFalse(GameState.state().isAllowedToViewOwnThrow());
            assertFalse(GameState.state().isBlindPass());

            if (expectedCall == null) {
                assertNull(GameState.state().getLatestCall());
            } else {
                assertEquals(expectedCall, GameState.state().getLatestCall().getNumberCombination());
            }
        }

        @Override
        public void assertPlayersInGameState(Player expectedCurrentPlayer) {
            Player[] players = GameState.state().getPlayers();
            Player leon = players[0];
            assertEquals("Leon", leon.getName());
            assertEquals(3, leon.getLives());

            Player dirk = players[1];
            assertEquals("Dirk", dirk.getName());
            assertEquals(2, dirk.getLives());

            Player currentPlayer = GameState.state().getCurrentPlayer();
            assertEquals(expectedCurrentPlayer, currentPlayer);
        }
    },
    NOT_BELIEVE_CORRECT_LEFT_DICE_OUT {

        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, GameState.state().getCallInput());
            assertFalse(GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
            assertFalse(GameState.state().getCup().isWatchingOwnThrow());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertTrue(GameState.state().isAllowedToThrow());
            assertFalse(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertFalse(GameState.state().isAllowedToViewOwnThrow());
            assertFalse(GameState.state().isBlindPass());

            if (expectedCall == null) {
                assertNull(GameState.state().getLatestCall());
            } else {
                assertEquals(expectedCall, GameState.state().getLatestCall().getNumberCombination());
            }
        }

        @Override
        public void assertPlayersInGameState(Player expectedCurrentPlayer) {
            Player[] players = GameState.state().getPlayers();
            Player leon = players[0];
            assertEquals("Leon", leon.getName());
            assertEquals(2, leon.getLives());

            Player dirk = players[1];
            assertEquals("Dirk", dirk.getName());
            assertEquals(3, dirk.getLives());

            Player currentPlayer = GameState.state().getCurrentPlayer();
            assertEquals(expectedCurrentPlayer, currentPlayer);
        }

        @Override
        public void assertDiceLocations(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors, Group dicesBeforeCupActors) {
            // Assert if the dices have been added to the correct group.
            SnapshotArray<Actor> dicesBeforeCup = dicesBeforeCupActors.getChildren();
            assertEquals(left, dicesBeforeCup.get(0));

            SnapshotArray<Actor> dicesUnderCup = dicesUnderCupActors.getChildren();
            assertEquals(middle, dicesUnderCup.get(0));
            assertEquals(right, dicesUnderCup.get(1));
        }

        @Override
        public void assertDices(NumberCombination expectedNumberCombination) {
            assertExpectedNumberCombinationWithGameState(expectedNumberCombination);
            assertFalse(GameState.state().getLeftDice().isUnderCup());
            assertTrue(GameState.state().getMiddleDice().isUnderCup());
            assertTrue(GameState.state().getRightDice().isUnderCup());

            assertFalse(GameState.state().getLeftDice().isLocked());
            assertFalse(GameState.state().getMiddleDice().isLocked());
            assertFalse(GameState.state().getRightDice().isLocked());
        }
    },
    NOT_BELIEVE_INCORRECT_LEFT_DICE_OUT {
        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, GameState.state().getCallInput());
            assertFalse(GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
            assertFalse(GameState.state().getCup().isWatchingOwnThrow());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertTrue(GameState.state().isAllowedToThrow());
            assertFalse(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertFalse(GameState.state().isAllowedToViewOwnThrow());
            assertFalse(GameState.state().isBlindPass());

            if (expectedCall == null) {
                assertNull(GameState.state().getLatestCall());
            } else {
                assertEquals(expectedCall, GameState.state().getLatestCall().getNumberCombination());
            }
        }

        @Override
        public void assertPlayersInGameState(Player expectedCurrentPlayer) {
            Player[] players = GameState.state().getPlayers();
            Player leon = players[0];
            assertEquals("Leon", leon.getName());
            assertEquals(3, leon.getLives());

            Player dirk = players[1];
            assertEquals("Dirk", dirk.getName());
            assertEquals(2, dirk.getLives());

            Player currentPlayer = GameState.state().getCurrentPlayer();
            assertEquals(expectedCurrentPlayer, currentPlayer);
        }

        @Override
        public void assertDiceLocations(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors, Group dicesBeforeCupActors) {
            // Assert if the dices have been added to the correct group.
            SnapshotArray<Actor> dicesBeforeCup = dicesBeforeCupActors.getChildren();
            assertEquals(left, dicesBeforeCup.get(0));

            SnapshotArray<Actor> dicesUnderCup = dicesUnderCupActors.getChildren();
            assertEquals(middle, dicesUnderCup.get(0));
            assertEquals(right, dicesUnderCup.get(1));
        }

        @Override
        public void assertDices(NumberCombination expectedNumberCombination) {
            assertExpectedNumberCombinationWithGameState(expectedNumberCombination);
            assertFalse(GameState.state().getLeftDice().isUnderCup());
            assertTrue(GameState.state().getMiddleDice().isUnderCup());
            assertTrue(GameState.state().getRightDice().isUnderCup());

            assertFalse(GameState.state().getLeftDice().isLocked());
            assertFalse(GameState.state().getMiddleDice().isLocked());
            assertFalse(GameState.state().getRightDice().isLocked());
        }
    },
    AFTER_BELIEVE_ALL_DICES_UNDER_CUP_CLOSE_CUP {
        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, GameState.state().getCallInput());
            assertFalse(GameState.state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(GameState.state().getCup().isWatchingOwnThrow());
            assertFalse(GameState.state().getCup().isBelieving());
            assertFalse(GameState.state().getCup().isLocked());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertTrue(GameState.state().isAllowedToThrow()); // First has to close the cup.
            assertFalse(GameState.state().isAllowedToBelieveOrNotBelieve());
            assertTrue(GameState.state().isAllowedToViewOwnThrow());
            assertFalse(GameState.state().isBlindPass());
            assertEquals(expectedCall, GameState.state().getLatestCall().getNumberCombination());
        }
    }
}
