package net.leejjon.bluffpoker.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import net.leejjon.bluffpoker.BlufPokerGame;

/**
 * Created by Leejjon on 2-10-2015.
 */
public class LiftCupAction extends Action {

    @Override
    public boolean act(float delta) {
        boolean actionDone = false;

        /**
         * This will move the cup up one pixel by frame until the cup moves completely out of the screen.
         *
         * I don't care about this being slow on phones who display less than 30 frames per second and this
         * being faster on phones with more frames per second.
         */
        if (actor.getY() < (Gdx.graphics.getHeight() / BlufPokerGame.getDivideScreenByThis())) {
            actor.moveBy(0f, 2f);
        } else {
            actionDone = true;
        }

        return actionDone;
    }
}
