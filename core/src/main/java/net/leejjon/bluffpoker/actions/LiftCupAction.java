package net.leejjon.bluffpoker.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import net.leejjon.bluffpoker.BluffPokerApp;

import java.util.concurrent.atomic.AtomicBoolean;

public class LiftCupAction extends Action {
    private AtomicBoolean done = new AtomicBoolean(false);

    @Override
    public boolean act(float delta) {
        /**
         * This will move the cup up one pixel by frame until the cup moves completely out of the screen.
         *
         * I don't care about this being slow on phones who display less than 30 frames per second and this
         * being faster on phones with more frames per second.
         */
        if (actor.getY() < (Gdx.graphics.getHeight() / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor())) {
            actor.moveBy(0f, 2f);
        } else {
            done.set(true);
        }

        return done.get();
    }
}
