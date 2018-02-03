package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import net.leejjon.bluffpoker.actors.DiceActor;
import net.leejjon.bluffpoker.ui.ClickableLabel;

public interface UserInterfaceAssertor {
    void assertDices(DiceActor left, DiceActor middle, DiceActor right);

    void assertCallBoard(Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton);

    // TODO: Implement when the player overview screen has been implemented.
//    void assertPlayers(GameState gameState);

    default void assertUserInterfaceState(GameState gameState, Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
        assertDices(gameState.getLeftDice().getDiceActor(), gameState.getMiddleDice().getDiceActor(), gameState.getMiddleDice().getDiceActor());
        assertCallBoard(callInputLabel, autoButton, callButton);
    }
}
