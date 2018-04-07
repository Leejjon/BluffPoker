package net.leejjon.bluffpoker.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;

import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.interfaces.StageInterface;
import net.leejjon.bluffpoker.stages.PauseStage;

import java.util.concurrent.atomic.AtomicBoolean;

public class FancyOpenPauseMenuListener extends InputMultiplexer {
    private AtomicBoolean pauseMenuGestureActivated = new AtomicBoolean(false);
    private final StageInterface stageInterface;

    public FancyOpenPauseMenuListener(StageInterface stageInterface) {
        this.stageInterface = stageInterface;
        addProcessor(new ActivateAdaptor());
        addProcessor(new GestureDetector(new FlingAdaptor()));
    }

    private class ActivateAdaptor extends InputAdapter {

        private final int leftAreaApplicableForSwipe;

        public ActivateAdaptor() {
            this.leftAreaApplicableForSwipe = Gdx.graphics.getWidth() / 10;
        }

        @Override
        public boolean touchDown(int x, int y, int pointer, int button) {
            if (x >= 0 && x < leftAreaApplicableForSwipe && !pauseMenuGestureActivated.weakCompareAndSet(false, true)) {
                stageInterface.openPauseScreen(x);
                return true;
            }
            return super.touchDown(x, y, pointer, button);
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (pauseMenuGestureActivated.get()) {
                stageInterface.openPauseScreen(screenX);
            }
            return super.touchDragged(screenX, screenY, pointer);
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {

            return super.touchUp(screenX, screenY, pointer, button);
        }
    }

    public class FlingAdaptor extends GestureDetector.GestureAdapter {
        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            if (pauseMenuGestureActivated.get()) { // Adjust fling to detect whether the fling actually starts on the menu.
                if (Math.abs(velocityX) > Math.abs(velocityY)) {
                    if (velocityX > 0) {
                        // TODO: Create action to move menu to the correct place.
                        Gdx.app.log(BluffPokerApp.TAG, "Got hit!");
                        return true;
                    } else {
                        // TODO: Create action to close the menu.
                        stageInterface.closePauseScreen();
                        return true;
                    }
                }
            }
            return super.fling(velocityX, velocityY, button);
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            if (x > PauseStage.getRawMenuWidth()) {
                // TODO: Create action to close the menu.
                stageInterface.closePauseScreen();
                return true;
            }
            return super.tap(x, y, count, button);
        }
    }
}
