package net.leejjon.bluffpoker.interfaces;

import net.leejjon.bluffpoker.enums.TutorialMessage;

public interface UserInterface {
    void call(String call);

    void forfeit();

    void finishGame(String winner);

    void showTutorialMessage(TutorialMessage message, String ... arguments);

    void showLockMessage();
}
