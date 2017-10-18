package net.leejjon.bluffpoker.enums;

import lombok.Getter;

public enum TutorialMessage {
    DISABLED_TUTORIAL("You have disabled the tutorial. You can always turn it back on again from the settings screen."),
    PLAYER_EXPLANATION("Bluff Poker is a local offline dice game you play with 2-10 players. It is played on one phone, which you pass to the next player every turn.");

    @Getter
    private final String message;

    TutorialMessage(String messsage) {
        this.message = messsage;
    }
}
