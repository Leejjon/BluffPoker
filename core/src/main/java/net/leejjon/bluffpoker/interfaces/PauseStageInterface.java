package net.leejjon.bluffpoker.interfaces;

public interface PauseStageInterface {
    float getRightSideOfMenuX();

    void setRightSideOfMenuX(float x);

    void continueOpeningPauseMenu();

    void continueClosingPauseMenu();

    boolean hasOpenPauseMenuActionRunning();

    boolean hasClosePauseMenuActionRunning();

    void doneOpeningPauseMenu();

    void doneClosingPauseMenu();
}
