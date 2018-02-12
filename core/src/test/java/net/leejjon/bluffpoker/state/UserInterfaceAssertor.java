package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import net.leejjon.bluffpoker.actors.CupActor;
import net.leejjon.bluffpoker.actors.DiceActor;
import net.leejjon.bluffpoker.ui.ClickableLabel;

public interface UserInterfaceAssertor {
    void assertCallBoard(Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton);

    void assertCup(CupActor cupActor);

    void assertDices(DiceActor left, DiceActor middle, DiceActor right, Group dicesUnderCupActors);

    // TODO: Implement when the player overview screen has been implemented.
//    void assertPlayers(GameState gameState);

    default void assertUserInterfaceState(GameState gameState, Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton, Group dicesUnderCupActors) {
        assertCallBoard(callInputLabel, autoButton, callButton);
        assertCup(gameState.getCup().getCupActor());
        assertDices(gameState.getLeftDice().getDiceActor(), gameState.getMiddleDice().getDiceActor(), gameState.getRightDice().getDiceActor(), dicesUnderCupActors);
    }
}
