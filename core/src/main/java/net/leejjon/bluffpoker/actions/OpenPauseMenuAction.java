package net.leejjon.bluffpoker.actions;

import com.badlogic.gdx.scenes.scene2d.Action;

import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.interfaces.PauseStageInterface;
import net.leejjon.bluffpoker.stages.PauseStage;

import java.util.concurrent.atomic.AtomicBoolean;

public class OpenPauseMenuAction extends Action {
    private AtomicBoolean done = new AtomicBoolean(false);
    private PauseStageInterface pauseStageInterface;
    private float incrementValue = 4f;
    private final float incrementPercentage = 1.1f;

    public OpenPauseMenuAction(PauseStageInterface pauseStageInterface) {
        this.pauseStageInterface = pauseStageInterface;
    }

    @Override
    public boolean act(float delta) {
        final float rightSideOfMenuX = pauseStageInterface.getRightSideOfMenuX();
        final float predictedIncrementValue = incrementValue * incrementPercentage;
        final float increasedValue = rightSideOfMenuX + predictedIncrementValue;
        if (increasedValue <= PauseStage.getRawMenuWidth() && pauseStageInterface.hasOpenPauseMenuActionRunning()) {
            incrementValue = predictedIncrementValue;
            pauseStageInterface.setRightSideOfMenuX(increasedValue);
        } else {
            pauseStageInterface.doneOpeningPauseMenu();
            done.set(true);
        }

        return done.get();
    }
}
