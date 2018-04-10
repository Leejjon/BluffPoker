package net.leejjon.bluffpoker.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;

import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.interfaces.PauseStageInterface;
import net.leejjon.bluffpoker.interfaces.StageInterface;
import net.leejjon.bluffpoker.stages.PauseStage;

import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Getter;

public class FancyOpenPauseMenuListener extends InputMultiplexer {
    @Getter
    private AtomicBoolean pauseMenuGestureActivated = new AtomicBoolean(false);
    private final StageInterface stageInterface;
    private final PauseStageInterface pauseStageInterface;

    public FancyOpenPauseMenuListener(StageInterface stageInterface, PauseStageInterface pauseStageInterface) {
        this.stageInterface = stageInterface;
        this.pauseStageInterface = pauseStageInterface;
        addProcessor(new GestureDetector(new FlingAdaptor()));
        addProcessor(new ActivateAdaptor());
    }

    private class ActivateAdaptor extends InputAdapter {

        private final int leftAreaApplicableForSwipe;

        public ActivateAdaptor() {
            this.leftAreaApplicableForSwipe = Gdx.graphics.getWidth() / 10;
        }

        @Override
        public boolean touchDown(int x, int y, int pointer, int button) {
            if (x >= 0 && x < leftAreaApplicableForSwipe && !pauseMenuGestureActivated.weakCompareAndSet(false, true)) {
                stageInterface.startOpeningPauseScreen(x);
                return true;
            }
            return super.touchDown(x, y, pointer, button);
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (pauseMenuGestureActivated.get()) {
                pauseStageInterface.setRightSideOfMenuX(screenX);
            }
            return super.touchDragged(screenX, screenY, pointer);
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            boolean didTheOtherUiDetectAnything = super.touchUp(screenX, screenY, pointer, button);

            int halfOfRawMenuWidth = PauseStage.getRawMenuWidth() / 2;
            
            if (wasRightFling.compareAndSet(true, false)) {
                Gdx.app.debug(BluffPokerApp.TAG, "Open pause menu after swipe right");
                pauseStageInterface.continueOpeningPauseMenu();
                return true;
            } else if (!didTheOtherUiDetectAnything && pauseMenuGestureActivated.get() && screenX < halfOfRawMenuWidth) {
                Gdx.app.debug(BluffPokerApp.TAG, "Close pause menu release before half");
                pauseStageInterface.continueClosingPauseMenu();
                return true;
            } else if (!didTheOtherUiDetectAnything  && pauseMenuGestureActivated.get() && screenX >= halfOfRawMenuWidth) {
                Gdx.app.debug(BluffPokerApp.TAG, "Open pause menu release over half");
                pauseStageInterface.continueOpeningPauseMenu();
                return true;
            } else {
                return false;
            }
        }
    }

    private AtomicBoolean wasRightFling = new AtomicBoolean(false);

    public class FlingAdaptor extends GestureDetector.GestureAdapter {
        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            if (pauseMenuGestureActivated.get()) { // Adjust fling to detect whether the fling actually starts on the menu.
                if (Math.abs(velocityX) > Math.abs(velocityY)) {
                    if (velocityX > 0) {
                        Gdx.app.debug(BluffPokerApp.TAG, "Swipe right");
                        wasRightFling.set(true);
                        return super.fling(velocityX, velocityY, button);
                    } else {
                        Gdx.app.debug(BluffPokerApp.TAG, "Swipe left");
                        pauseStageInterface.continueClosingPauseMenu();
                        return true;
                    }
                }
            }
            return super.fling(velocityX, velocityY, button);
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            if (x > PauseStage.getRawMenuWidth()) {
                Gdx.app.debug(BluffPokerApp.TAG, "Closed pause menu after tap in right area.");
                pauseStageInterface.continueClosingPauseMenu();
                return true;
            }
            return super.tap(x, y, count, button);
        }
    }
}
