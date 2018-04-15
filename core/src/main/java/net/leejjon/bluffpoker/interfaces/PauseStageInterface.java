package net.leejjon.bluffpoker.interfaces;

public interface PauseStageInterface {
    float getRightSideOfMenuX();

    void setRightSideOfMenuX(float x);

    void continueOpeningPauseMenu();

    void continueClosingPauseMenu();

    boolean hasOpenPauseMenuActionRunning();

    boolean hasClosePauseMenuActionRunning();

    boolean isPauseMenuGestureActivated();

    boolean activatePauseMenuGesture();

    boolean isMenuOpen();

    void doneOpeningPauseMenu();

    void doneClosingPauseMenu();
}
