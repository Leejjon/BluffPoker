package net.leejjon.bluffpoker.enums;

import lombok.Getter;

public enum TutorialMessage {
    DISABLED_TUTORIAL("You have disabled the tutorial. You can always turn it back on again from the settings screen."),
    ORDERING_PLAYERS("When you play the game with more than two players, the order of players becomes important. Swap players in the desired order with the up and down buttons. Put the player(A) that you want to start the game on top. Put the player(B) that sits next to player A as second. Player(C) that sits next to player B third and so on."),
    PLAYER_EXPLANATION("This app is a mobile version of a dice game we call \"Bluff Poker\". It is played with 2-10 players using a cup and three dices. You need one phone and one or more friends. Pass the phone to the next player every turn. Before you start you first have to enter the player names."),
    GAME_START("Read the console messages at the bottom for the main game information. Right now it says, \"Shake the cup, %s\", which means you have to shake your actual phone! You should hear a dice sound confirming the throw.");

    @Getter
    private final String message;

    TutorialMessage(String messsage) {
        this.message = messsage;
    }
}
