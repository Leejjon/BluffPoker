package net.leejjon.bluffpoker.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;

import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.interfaces.StageInterface;

import java.util.concurrent.atomic.AtomicBoolean;

public class SwipeFromLeftGestureDetector extends GestureDetector {
    private final int leftAreaApplicableForSwipe;

    private AtomicBoolean ignoreFling = new AtomicBoolean(true);

    public SwipeFromLeftGestureDetector(StageInterface stageInterface) {
        super(new GestureDetector.GestureAdapter() {
            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                if (Math.abs(velocityX) > Math.abs(velocityY)) {
                    if (velocityX > 0) {
                        Gdx.app.log(BluffPokerApp.TAG, "Open the menu.");
                        stageInterface.openPauseScreen();
                        return true;
                    }
                }
                return false;
            }
        });

        // Get 16.6% of the left side of the screen to be swipable.
        leftAreaApplicableForSwipe = (Gdx.graphics.getWidth() / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor()) / 6;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if (x >= 0 && x < leftAreaApplicableForSwipe) {
            Gdx.app.log(BluffPokerApp.TAG, "Fling detected, area size: " + leftAreaApplicableForSwipe);
            ignoreFling.lazySet(false);
            return super.touchDown(x, y, pointer, button);
        } else {
            return false;
        }
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (ignoreFling.get()) {
            return false;
        } else {
            ignoreFling.lazySet(true);
            return super.touchUp(x, y, pointer, button);
        }
    }
}
