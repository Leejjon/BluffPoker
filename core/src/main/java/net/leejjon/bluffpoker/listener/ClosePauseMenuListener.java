package net.leejjon.bluffpoker.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;

import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.interfaces.PauseStageInterface;
import net.leejjon.bluffpoker.interfaces.StageInterface;
import net.leejjon.bluffpoker.stages.PauseStage;

public class ClosePauseMenuListener extends GestureDetector {
    public ClosePauseMenuListener(PauseStageInterface pauseStageInterface) {
        super(new GestureDetector.GestureAdapter() {
            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                if (Math.abs(velocityX) > Math.abs(velocityY)) {
                    if (velocityX <= 0) {
                        Gdx.app.log(BluffPokerApp.TAG, "Log from closePauseMenuListener.fling");
                        pauseStageInterface.continueClosingPauseMenu();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                if (x / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor() > PauseStage.getMenuWidth()) {
                    Gdx.app.log(BluffPokerApp.TAG, "Log from closePauseMenuListener.tap x=" + x + " getmenuwidth=" + PauseStage.getMenuWidth());
                    pauseStageInterface.continueClosingPauseMenu();
                    return true;
                }

                return super.tap(x, y, count, button);
            }
        });
    }
}
