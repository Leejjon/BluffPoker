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

import static net.leejjon.bluffpoker.state.GameState.state;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public enum GameStateEnum implements GameStateAssertor, UserInterfaceAssertor {
    NEW_GAME {
        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, state().getCallInput());
            assertEquals(false, state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertTrue(state().isAllowedToThrow());
            assertFalse(state().isAllowedToBelieveOrNotBelieve());
            assertFalse(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());
            assertFalse(state().isBelieved666());
            assertEquals(expectedCall, state().getLatestCall());
        }
    },
    AFTER_FIRST_SHAKE {
        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, state().getCallInput());
            assertTrue(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(state().isAllowedToThrow());
            assertFalse(state().isAllowedToBelieveOrNotBelieve());
            assertTrue(state().isAllowedToViewOwnThrow());
            assertTrue(state().isBlindPass());
            assertFalse(state().isBelieved666());

            // Both are null in this case.
            assertEquals(expectedCall, state().getLatestCall());
        }

        @Override
        public void assertCallBoardInUI(String expectedCall, Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
            assertEquals(expectedCall, callInputLabel.getText().toString());
            assertFalse(autoButton.isDisabled());
            assertFalse(callButton.isDisabled());
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
            assertEquals(expectedCall, state().getCallInput());
            assertTrue(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertTrue(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(state().isAllowedToThrow());
            assertFalse(state().isAllowedToBelieveOrNotBelieve());
            assertTrue(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());
            assertFalse(state().isBelieved666());
            assertEquals(expectedCall, state().getLatestCall());
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
            assertEquals(expectedCall, state().getCallInput());
            assertFalse(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(state().isAllowedToThrow());
            assertTrue(state().isAllowedToBelieveOrNotBelieve());
            assertFalse(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());
            assertFalse(state().isBelieved666());
            assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
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
            assertEquals(expectedCall, state().getCallInput());
            assertFalse(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(state().isAllowedToThrow());
            assertTrue(state().isAllowedToBelieveOrNotBelieve());
            assertFalse(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());
            assertFalse(state().isBelieved666());
            assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
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
            assertEquals(expectedCall, state().getCallInput());
            assertTrue(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertTrue(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(state().isAllowedToThrow());
            assertFalse(state().isAllowedToBelieveOrNotBelieve());
            assertTrue(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());
            assertFalse(state().isBelieved666());
            assertEquals(expectedCall, state().getLatestCall());
        }

        @Override
        public void assertDiceLocations(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors, Group dicesBeforeCupActors) {
            SnapshotArray<Actor> dicesBeforeCup = dicesBeforeCupActors.getChildren();
            dicesBeforeCup.contains(left, true);

            SnapshotArray<Actor> dicesUnderCup = dicesUnderCupActors.getChildren();
            dicesUnderCup.contains(middle, true);
            dicesUnderCup.contains(right, true);
        }

        @Override
        public void assertDices(NumberCombination expectedNumberCombination) {
            assertExpectedNumberCombinationWithGameState(expectedNumberCombination);
            assertFalse(state().getLeftDice().isUnderCup());
            assertTrue(state().getMiddleDice().isUnderCup());
            assertTrue(state().getRightDice().isUnderCup());

            assertFalse(state().getLeftDice().isLocked());
            assertFalse(state().getMiddleDice().isLocked());
            assertFalse(state().getRightDice().isLocked());
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
            assertEquals(expectedCall, state().getCallInput());
            assertFalse(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(state().isAllowedToThrow());
            assertTrue(state().isAllowedToBelieveOrNotBelieve());
            assertFalse(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());
            assertFalse(state().isBelieved666());
            assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
        }

        @Override
        public void assertDiceLocations(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors, Group dicesBeforeCupActors) {
            SnapshotArray<Actor> dicesBeforeCup = dicesBeforeCupActors.getChildren();
            dicesBeforeCup.contains(left, true);

            SnapshotArray<Actor> dicesUnderCup = dicesUnderCupActors.getChildren();
            dicesUnderCup.contains(middle, true);
            dicesUnderCup.contains(right, true);
        }

        @Override
        public void assertDices(NumberCombination expectedNumberCombination) {
            assertExpectedNumberCombinationWithGameState(expectedNumberCombination);
            assertFalse(state().getLeftDice().isUnderCup());
            assertTrue(state().getMiddleDice().isUnderCup());
            assertTrue(state().getRightDice().isUnderCup());

            assertFalse(state().getLeftDice().isLocked());
            assertFalse(state().getMiddleDice().isLocked());
            assertFalse(state().getRightDice().isLocked());
        }
    },
    BELIEVED_ALL_DICES_UNDER_CUP {
        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, state().getCallInput());
            assertFalse(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertTrue(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(state().isAllowedToThrow()); // First has to close the cup.
            assertTrue(state().isAllowedToBelieveOrNotBelieve());
            assertFalse(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());
            assertFalse(state().isBelieved666());
            assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
        }

        @Override
        public void assertCupInUI(CupActor cupActor) {
            assertEquals(cupActor.getOpenCupDrawable(), cupActor.getCupImage().getDrawable());
            assertFalse(cupActor.getLockImage().isVisible());
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
            assertEquals(expectedCall, state().getCallInput());
            assertFalse(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertTrue(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(state().isAllowedToThrow()); // First has to close the cup.
            assertTrue(state().isAllowedToBelieveOrNotBelieve());
            assertFalse(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());
            assertFalse(state().isBelieved666());
            assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
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
            dicesBeforeCup.contains(left, true);

            SnapshotArray<Actor> dicesUnderCup = dicesUnderCupActors.getChildren();
            dicesUnderCup.contains(middle, true);
            dicesUnderCup.contains(right, true);
        }

        @Override
        public void assertDices(NumberCombination expectedNumberCombination) {
            assertExpectedNumberCombinationWithGameState(expectedNumberCombination);
            assertFalse(state().getLeftDice().isUnderCup());
            assertTrue(state().getMiddleDice().isUnderCup());
            assertTrue(state().getRightDice().isUnderCup());

            assertTrue(state().getLeftDice().isLocked());
            assertFalse(state().getMiddleDice().isLocked());
            assertFalse(state().getRightDice().isLocked());
        }
    },
    BELIEVED_ALL_DICES_UNDER_CUP_BLIND {
        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, state().getCallInput());
            assertTrue(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isBelieving());
            assertTrue(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(state().isAllowedToThrow());
            assertFalse(state().isAllowedToBelieveOrNotBelieve());
            assertFalse(state().isAllowedToViewOwnThrow());
            assertTrue(state().isBlindPass());
            assertFalse(state().isBelieved666());
            assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
        }

        @Override
        public void assertCupInUI(CupActor cupActor) {
            assertEquals(cupActor.getClosedCupDrawable(), cupActor.getCupImage().getDrawable());
            assertTrue(cupActor.getLockImage().isVisible());
        }

        @Override
        public void assertCallBoardInUI(String expectedCall, Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
            assertEquals(expectedCall, callInputLabel.getText().toString());
            assertFalse(autoButton.isDisabled());
            assertFalse(callButton.isDisabled());
        }
    },
    BELIEVED_666_ALL_DICES_UNDER_CUP {
        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, state().getCallInput());
            assertFalse(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertTrue(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(state().isAllowedToThrow()); // First has to close the cup.
            assertTrue(state().isAllowedToBelieveOrNotBelieve());
            assertFalse(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());
            assertTrue(state().isBelieved666());
            assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
        }

        @Override
        public void assertCupInUI(CupActor cupActor) {
            assertEquals(cupActor.getOpenCupDrawable(), cupActor.getCupImage().getDrawable());
            assertFalse(cupActor.getLockImage().isVisible());
        }
    },
    BELIEVED_666_ALL_DICES_UNDER_CUP_CLOSED {
        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, state().getCallInput());
            assertFalse(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertTrue(state().isAllowedToThrow()); // First has to close the cup.
            assertFalse(state().isAllowedToBelieveOrNotBelieve());
            assertTrue(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());
            assertTrue(state().isBelieved666());
            assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
        }
    },
    BELIEVED_666_ALL_DICES_UNDER_CUP_CLOSED_THROWN {
        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, state().getCallInput());
            assertTrue(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(state().isAllowedToThrow());
            assertFalse(state().isAllowedToBelieveOrNotBelieve());
            assertTrue(state().isAllowedToViewOwnThrow());
            assertTrue(state().isBlindPass());
            assertTrue(state().isBelieved666());
            assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
        }

        @Override
        public void assertCallBoardInUI(String expectedCall, Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
            assertEquals(expectedCall, callInputLabel.getText().toString());
            assertFalse(autoButton.isDisabled());
            assertFalse(callButton.isDisabled());
        }
    },
    NOT_BELIEVE_CORRECT_All_DICES_UNDER_CUP {

        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, state().getCallInput());
            assertFalse(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertTrue(state().isAllowedToThrow());
            assertFalse(state().isAllowedToBelieveOrNotBelieve());
            assertFalse(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());

            if (expectedCall == null) {
                assertNull(state().getLatestCall());
            } else {
                assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
            }
        }

        @Override
        public void assertPlayersInGameState(Player expectedCurrentPlayer) {
            Player[] players = state().getPlayers();
            Player leon = players[0];
            assertEquals("Leon", leon.getName());
            assertEquals(2, leon.getLives());

            Player dirk = players[1];
            assertEquals("Dirk", dirk.getName());
            assertEquals(3, dirk.getLives());

            Player currentPlayer = state().getCurrentPlayer();
            assertEquals(expectedCurrentPlayer, currentPlayer);
        }
    },
    NOT_BELIEVE_INCORRECT_ALL_DICES_UNDER_CUP {

        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, state().getCallInput());
            assertFalse(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertTrue(state().isAllowedToThrow());
            assertFalse(state().isAllowedToBelieveOrNotBelieve());
            assertFalse(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());

            if (expectedCall == null) {
                assertNull(state().getLatestCall());
            } else {
                assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
            }
        }

        @Override
        public void assertPlayersInGameState(Player expectedCurrentPlayer) {
            Player[] players = state().getPlayers();
            Player leon = players[0];
            assertEquals("Leon", leon.getName());
            assertEquals(3, leon.getLives());

            Player dirk = players[1];
            assertEquals("Dirk", dirk.getName());
            assertEquals(2, dirk.getLives());

            Player currentPlayer = state().getCurrentPlayer();
            assertEquals(expectedCurrentPlayer, currentPlayer);
        }
    },
    NOT_BELIEVE_CORRECT_LEFT_DICE_OUT {

        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, state().getCallInput());
            assertFalse(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertTrue(state().isAllowedToThrow());
            assertFalse(state().isAllowedToBelieveOrNotBelieve());
            assertFalse(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());

            if (expectedCall == null) {
                assertNull(state().getLatestCall());
            } else {
                assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
            }
        }

        @Override
        public void assertPlayersInGameState(Player expectedCurrentPlayer) {
            Player[] players = state().getPlayers();
            Player leon = players[0];
            assertEquals("Leon", leon.getName());
            assertEquals(2, leon.getLives());

            Player dirk = players[1];
            assertEquals("Dirk", dirk.getName());
            assertEquals(3, dirk.getLives());

            Player currentPlayer = state().getCurrentPlayer();
            assertEquals(expectedCurrentPlayer, currentPlayer);
        }

        @Override
        public void assertDiceLocations(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors, Group dicesBeforeCupActors) {
            SnapshotArray<Actor> dicesBeforeCup = dicesBeforeCupActors.getChildren();
            dicesBeforeCup.contains(left, true);

            SnapshotArray<Actor> dicesUnderCup = dicesUnderCupActors.getChildren();
            dicesUnderCup.contains(middle, true);
            dicesUnderCup.contains(right, true);
        }

        @Override
        public void assertDices(NumberCombination expectedNumberCombination) {
            assertExpectedNumberCombinationWithGameState(expectedNumberCombination);
            assertFalse(state().getLeftDice().isUnderCup());
            assertTrue(state().getMiddleDice().isUnderCup());
            assertTrue(state().getRightDice().isUnderCup());

            assertFalse(state().getLeftDice().isLocked());
            assertFalse(state().getMiddleDice().isLocked());
            assertFalse(state().getRightDice().isLocked());
        }
    },
    NOT_BELIEVE_INCORRECT_LEFT_DICE_OUT {
        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, state().getCallInput());
            assertFalse(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertTrue(state().isAllowedToThrow());
            assertFalse(state().isAllowedToBelieveOrNotBelieve());
            assertFalse(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());

            if (expectedCall == null) {
                assertNull(state().getLatestCall());
            } else {
                assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
            }
        }

        @Override
        public void assertPlayersInGameState(Player expectedCurrentPlayer) {
            Player[] players = state().getPlayers();
            Player leon = players[0];
            assertEquals("Leon", leon.getName());
            assertEquals(3, leon.getLives());

            Player dirk = players[1];
            assertEquals("Dirk", dirk.getName());
            assertEquals(2, dirk.getLives());

            Player currentPlayer = state().getCurrentPlayer();
            assertEquals(expectedCurrentPlayer, currentPlayer);
        }

        @Override
        public void assertPlayersInUI() {
            // TODO:
        }

        @Override
        public void assertDiceLocations(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors, Group dicesBeforeCupActors) {
            SnapshotArray<Actor> dicesBeforeCup = dicesBeforeCupActors.getChildren();
            dicesBeforeCup.contains(left, true);

            SnapshotArray<Actor> dicesUnderCup = dicesUnderCupActors.getChildren();
            dicesUnderCup.contains(middle, true);
            dicesUnderCup.contains(right, true);
        }

        @Override
        public void assertDices(NumberCombination expectedNumberCombination) {
            assertExpectedNumberCombinationWithGameState(expectedNumberCombination);
            assertFalse(state().getLeftDice().isUnderCup());
            assertTrue(state().getMiddleDice().isUnderCup());
            assertTrue(state().getRightDice().isUnderCup());

            assertFalse(state().getLeftDice().isLocked());
            assertFalse(state().getMiddleDice().isLocked());
            assertFalse(state().getRightDice().isLocked());
        }
    },
    AFTER_BELIEVE_ALL_DICES_UNDER_CUP_CLOSE_CUP {
        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, state().getCallInput());
            assertFalse(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isWatching());
            assertFalse(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertTrue(state().isAllowedToThrow()); // First has to close the cup.
            assertFalse(state().isAllowedToBelieveOrNotBelieve());
            assertTrue(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());
            assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
        }
    },
    AFTER_BELIEVE_ALL_DICES_UNDER_CUP_BLIND_PEEK {
        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, state().getCallInput());
            assertFalse(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertTrue(state().getCup().isWatching());
        }

        @Override
        public void assertCupInUI(CupActor cupActor) {
            assertEquals(cupActor.getOpenCupDrawable(), cupActor.getCupImage().getDrawable());
            assertFalse(cupActor.getLockImage().isVisible());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(state().isAllowedToThrow()); // First has to close the cup.
            assertFalse(state().isAllowedToBelieveOrNotBelieve());
            assertTrue(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());
            assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
        }

        @Override
        public void assertCallBoardInUI(String expectedCall, Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
            assertEquals(expectedCall, callInputLabel.getText().toString());
            assertTrue(autoButton.isDisabled());
            assertTrue(callButton.isDisabled());
        }
    },
    AFTER_BELIEVE_ALL_DICES_UNDER_CUP_BLIND_PEEK_CLOSE {
        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, state().getCallInput());
            assertFalse(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertFalse(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertCupInUI(CupActor cupActor) {
            assertEquals(cupActor.getClosedCupDrawable(), cupActor.getCupImage().getDrawable());
            assertFalse(cupActor.getLockImage().isVisible());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertTrue(state().isAllowedToThrow());
            assertFalse(state().isAllowedToBelieveOrNotBelieve());
            assertTrue(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());
            assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
        }

        @Override
        public void assertCallBoardInUI(String expectedCall, Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
            assertEquals(expectedCall, callInputLabel.getText().toString());
            assertTrue(autoButton.isDisabled());
            assertTrue(callButton.isDisabled());
        }
    },
    AFTER_BELIEVE_LEFT_SIX_OUT_PULL_BACK_LEFT_SIX {
        @Override
        public void assertCupInUI(CupActor cupActor) {
            assertEquals(cupActor.getOpenCupDrawable(), cupActor.getCupImage().getDrawable());
            assertFalse(cupActor.getLockImage().isVisible());
        }

        @Override
        public void assertCallBoardInGameState(String expectedCall) {
            assertEquals(expectedCall, state().getCallInput());
            assertFalse(state().isAllowedToCall());
        }

        @Override
        public void assertCupInGameState() {
            assertTrue(state().getCup().isBelieving());
            assertFalse(state().getCup().isLocked());
            assertFalse(state().getCup().isWatching());
        }

        @Override
        public void assertStatusses(NumberCombination expectedCall) {
            assertFalse(state().isAllowedToThrow()); // First has to close the cup.
            assertTrue(state().isAllowedToBelieveOrNotBelieve());
            assertFalse(state().isAllowedToViewOwnThrow());
            assertFalse(state().isBlindPass());
            assertFalse(state().isBelieved666());
            assertEquals(expectedCall, state().getLatestCall().getNumberCombination());
        }


        @Override
        public void assertDiceLocksInUI(DiceActor left, DiceActor middle, DiceActor right) {
            assertFalse(left.getLockImage().isVisible());
            assertFalse(middle.getLockImage().isVisible());
            assertFalse(right.getLockImage().isVisible());
        }

        @Override
        public void assertDiceLocations(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors, Group dicesBeforeCupActors) {
            // Assert if the dices have been added to the correct group.
            SnapshotArray<Actor> dicesBeforeCup = dicesBeforeCupActors.getChildren();
            dicesBeforeCup.contains(left, true);

            SnapshotArray<Actor> dicesUnderCup = dicesUnderCupActors.getChildren();
            dicesUnderCup.contains(middle, true);
            dicesUnderCup.contains(right, true);
        }

        @Override
        public void assertDices(NumberCombination expectedNumberCombination) {
            assertExpectedNumberCombinationWithGameState(expectedNumberCombination);
            assertTrue(state().getLeftDice().isUnderCup());
            assertTrue(state().getMiddleDice().isUnderCup());
            assertTrue(state().getRightDice().isUnderCup());

            assertFalse(state().getLeftDice().isLocked());
            assertFalse(state().getMiddleDice().isLocked());
            assertFalse(state().getRightDice().isLocked());
        }
    }
}
