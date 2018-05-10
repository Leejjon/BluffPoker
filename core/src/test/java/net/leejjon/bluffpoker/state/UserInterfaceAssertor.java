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
import net.leejjon.bluffpoker.ui.ScoreTableRow;

import java.util.List;

import static net.leejjon.bluffpoker.state.GameState.state;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public interface UserInterfaceAssertor {
    default void assertCallBoardInUI(String expectedCall, Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
        assertEquals(expectedCall, callInputLabel.getText().toString());
        assertTrue(autoButton.isDisabled());
        assertTrue(callButton.isDisabled());
    }

    default void assertCupInUI(CupActor cupActor) {
        assertEquals(cupActor.getClosedCupDrawable(), cupActor.getCupImage().getDrawable());
        assertFalse(cupActor.getLockImage().isVisible());
    }

    default void assertDicesInUI(Group dicesUnderCupActors, Group dicesBeforeCupActors, NumberCombination expectedValue) {
        DiceActor left = state().getLeftDice().getDiceActor();
        DiceActor middle = state().getMiddleDice().getDiceActor();
        DiceActor right = state().getRightDice().getDiceActor();

        assertDiceValuesInUI(left, middle, right, expectedValue);
        assertDiceLocksInUI(left, middle, right);
        assertDiceLocations(left, middle, right, dicesUnderCupActors, dicesBeforeCupActors);
    }

    default void assertDiceLocations(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors, Group dicesBeforeCupActors) {
        assertIfDicesAreInUnderCupGroup(left, middle, right, dicesUnderCupActors);
    }

    default void assertUserInterfaceState(String expectedCall, Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton, Group dicesUnderCupActors, Group dicesBeforeCupActors,
                                          NumberCombination expectedValue, Player expectedCurrentPlayer) {
        assertPlayersInUiMatchesWithState();
        assertCorrectCurrentPlayerInUI(expectedCurrentPlayer);
        assertCallBoardInUI(expectedCall, callInputLabel, autoButton, callButton);
        assertCupInUI(state().getCup().getCupActor());
        assertDicesInUI(dicesUnderCupActors, dicesBeforeCupActors, expectedValue);
    }

    default void assertCorrectCurrentPlayerInUI(Player expectedCurrentPlayer) {
        assertEquals(expectedCurrentPlayer.getName(), state().getCurrentPlayerLabel().getText().toString());
    }

    default void assertDiceValuesInUI(DiceActor left, DiceActor middle, DiceActor right, NumberCombination expectedValue) {
        // Default is 643, but -1 because of array index will make it 532.
        assertEquals(left.getSpriteDrawables()[expectedValue.getHighestNumber() - 1], left.getDiceImage().getDrawable());
        assertEquals(middle.getSpriteDrawables()[expectedValue.getMiddleNumber() - 1], middle.getDiceImage().getDrawable());
        assertEquals(right.getSpriteDrawables()[expectedValue.getLowestNumber() - 1], right.getDiceImage().getDrawable());
    }

    default void assertDiceLocksInUI(DiceActor left, DiceActor middle, DiceActor right) {
        assertFalse(left.getLockImage().isVisible());
        assertFalse(middle.getLockImage().isVisible());
        assertFalse(right.getLockImage().isVisible());
    }

    static void assertIfDicesAreInUnderCupGroup(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors) {
        // Assert if the dices have been added to the correct group.
        SnapshotArray<Actor> children = dicesUnderCupActors.getChildren();
        assertTrue(children.contains(left, true));
        assertTrue(children.contains(middle, true));
        assertTrue(children.contains(right, true));
    }

    static void assertPlayersInUiMatchesWithState() {
        List<ScoreTableRow> scoresList = state().getScores();

        for (int i = 0; i < scoresList.size(); i++) {
            ScoreTableRow str = scoresList.get(i);
            Player player = state().getPlayers().get(i);
            assertEquals(player.getName(), str.getPlayerName());
            assertEquals(player.isRidingOnTheBok() ? ScoreTableRow.ON_THE_BOK_ICON : String.valueOf(player.getLives()), str.getPlayerLives());
        }
    }
}
