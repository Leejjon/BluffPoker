package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.SnapshotArray;

import net.leejjon.bluffpoker.actors.CupActor;
import net.leejjon.bluffpoker.actors.DiceActor;
import net.leejjon.bluffpoker.logic.NumberCombination;
import net.leejjon.bluffpoker.ui.ClickableLabel;

import static org.junit.Assert.assertEquals;

public interface UserInterfaceAssertor {
    void assertCallBoard(Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton);

    void assertCup(CupActor cupActor);

    default void assertDices(Group dicesUnderCupActors, NumberCombination expectedValue) {
        DiceActor left = GameState.state().getLeftDice().getDiceActor();
        DiceActor middle = GameState.state().getMiddleDice().getDiceActor();
        DiceActor right = GameState.state().getRightDice().getDiceActor();

        // Default is 643, but -1 because of array index will make it 532.
        assertEquals(left.getSpriteDrawables()[expectedValue.getHighestNumber() - 1], left.getDiceImage().getDrawable());
        assertEquals(middle.getSpriteDrawables()[expectedValue.getMiddleNumber() - 1], middle.getDiceImage().getDrawable());
        assertEquals(right.getSpriteDrawables()[expectedValue.getLowestNumber() - 1], right.getDiceImage().getDrawable());
        assertDiceLocations(left, middle, right, dicesUnderCupActors);
    }

    default void assertDiceLocations(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors) {
        assertIfDicesAreInUnderCupGroup(left, middle, right, dicesUnderCupActors);
    }

    // TODO: Implement when the player overview screen has been implemented.
//    void assertPlayers(GameState gameState);

    default void assertUserInterfaceState(Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton, Group dicesUnderCupActors, NumberCombination expectedValue) {
        assertCallBoard(callInputLabel, autoButton, callButton);
        assertCup(GameState.state().getCup().getCupActor());
        assertDices(dicesUnderCupActors, expectedValue);
    }

    static void assertIfDicesAreInUnderCupGroup(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors) {
        // Assert if the dices have been added to the correct group.
        SnapshotArray<Actor> children = dicesUnderCupActors.getChildren();
        assertEquals(left, children.get(0));
        assertEquals(middle, children.get(1));
        assertEquals(right, children.get(2));
    }
}
