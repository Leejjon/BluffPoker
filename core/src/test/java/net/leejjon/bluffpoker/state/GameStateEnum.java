package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import net.leejjon.bluffpoker.actors.DiceActor;
import net.leejjon.bluffpoker.logic.Player;
import net.leejjon.bluffpoker.ui.ClickableLabel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        public void assertPlayers(GameState gameState) {
            Player currentPlayer = gameState.getCurrentPlayer();
            assertNotNull(currentPlayer);
            assertEquals("Leon", currentPlayer.getName());
            assertEquals(3, currentPlayer.getLives());
        }

        @Override
        public void assertDices(DiceActor left, DiceActor middle, DiceActor right) {
            // Default is 643, but -1 because of array index will make it 532.
            Drawable drawableOfDiceImage6 = left.getSpriteDrawables()[5];
            assertEquals(drawableOfDiceImage6, left.getDiceImage().getDrawable());

//            Drawable drawableOfDiceImage6 = left.getSpriteDrawables()[5];
//            assertEquals();
            // TODO:
        }

        @Override
        public void assertCallBoard(Label callInputLabel, ClickableLabel autoButton, ClickableLabel callButton) {
            assertEquals("000", callInputLabel.getText().toString());
            // TODO:
        }
    }
}
