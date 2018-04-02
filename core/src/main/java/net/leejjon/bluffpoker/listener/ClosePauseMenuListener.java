package net.leejjon.bluffpoker.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;

import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.interfaces.StageInterface;

public class ClosePauseMenuListener extends GestureDetector {
    public ClosePauseMenuListener(StageInterface stageInterface) {
        super(new GestureDetector.GestureAdapter() {
            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                if (Math.abs(velocityX) > Math.abs(velocityY)) {
                    if (velocityX <= 0) {
                        stageInterface.closePauseScreen();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                Gdx.app.log(BluffPokerApp.TAG, "Tap succesful");
                return super.tap(x, y, count, button);
            }
        });
    }
}
