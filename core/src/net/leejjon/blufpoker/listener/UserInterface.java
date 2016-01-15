package net.leejjon.blufpoker.listener;

import net.leejjon.blufpoker.NumberCombination;

/**
 * Created by Leejjon on 12-10-2015.
 */
public interface UserInterface {
    void log(String message);

    void finishGame(String winner);

    void restart();

    void enableCallUserInterface();

    void disableCallUserInterface();

    void resetCall();
}
