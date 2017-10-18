package net.leejjon.bluffpoker.interfaces;

import net.leejjon.bluffpoker.enums.TutorialMessage;

public interface UserInterface {
    void call();

    void enableCallUserInterface();

    void disableCallUserInterface();

    void finishGame(String winner);

    void log(String message);

    void resetCall();

    void restart();

    void setCallField(String call);

    void showTutorialMessage(TutorialMessage message, String ... arguments);
}
