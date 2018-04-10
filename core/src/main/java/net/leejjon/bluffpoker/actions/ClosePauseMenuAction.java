package net.leejjon.bluffpoker.actions;

import com.badlogic.gdx.scenes.scene2d.Action;

import net.leejjon.bluffpoker.interfaces.PauseStageInterface;

import java.util.concurrent.atomic.AtomicBoolean;

public class ClosePauseMenuAction extends Action {
    private AtomicBoolean done = new AtomicBoolean(false);
    private PauseStageInterface pauseStageInterface;

    private float decrementValue = 4f;
    private final float decrementPercentage = 1.1f;

    public ClosePauseMenuAction(PauseStageInterface pauseStageInterface) {
        this.pauseStageInterface = pauseStageInterface;
    }

    @Override
    public boolean act(float delta) {
        final float rightSideOfMenuX = pauseStageInterface.getRightSideOfMenuX();
        final float predictedDecrementValue = decrementValue * decrementPercentage;
        final float decreasedValue = rightSideOfMenuX - predictedDecrementValue;
        if (decreasedValue > 0 && pauseStageInterface.hasClosePauseMenuActionRunning()) {
            decrementValue = predictedDecrementValue;
            pauseStageInterface.setRightSideOfMenuX(decreasedValue);
        } else {
            pauseStageInterface.doneClosingPauseMenu();
            done.set(true);
        }

        return done.get();
    }
}
