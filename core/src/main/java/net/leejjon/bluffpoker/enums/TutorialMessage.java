package net.leejjon.bluffpoker.enums;

import lombok.Getter;
import net.leejjon.bluffpoker.logic.Game;

public enum TutorialMessage {
    BELIEVE_OR_NOT_BELIEVE("Pass the phone to %2$s. So %2$s, %1$s says he/she has thrown at least %3$s. Do you believe this?\n - If you believe this, give the cup a single tap.\n - If you don't believe this, swipe the cup up."),
    DID_NOT_BELIEVE_BLUFF("You saw through %1$s's bluff (%2$s) and didn't believe it! Now %1$s lost a life and must shake the phone to try again (if not out of lives)."),
    DID_NOT_BELIEVE_THRUTH("You didn't believe %1$s had thrown at least %2$s, but %1$s actually did and now you lost a life! Shake the phone to throw and start a new turn (if not out of lives)."),
    DISABLED_TUTORIAL("You have disabled the tutorial. You can always turn it back on again from the settings screen."),
    EXPLAIN_LOCK("The lock icon on the dice means it won't be thrown. You can toggle the lock on/off by tapping on the dice. Close the cup and shake the phone to throw the dices under the cup."),
    FIRST_THROWN_SINCE_DEATH("You threw all dices under the cup. Tap on the cup to take a look at your throw. \nPS: Don't show the other players!"),
    GAME_START("Read the console messages at the bottom for the main game information. Right now it says, \"" + Game.SHAKE_THE_CUP  + "\", which means %1$s has to shake this actual phone! You should hear a dice sound confirming the throw."),
    LOOKING_AT_OWN_THROW_FIRST_TURN_SINCE_DEATH("You have thrown %s, %s and %s. That is worth %4$s (when counting the throw value always put the highest number left and the lowest number right). Press the \"Call\" button to enter your call. Either enter %4$s to be safe or something higher if you think you can fool the next player to believe you."),
    LOOK_AT_OWN_THROW_THAT_IS_HIGHER("You can safely call what you have just thrown (%1$s), as it is higher than %2$s's call, which is: %3$s."),
    LOOK_AT_OWN_THROW_THAT_IS_LOWER("You just threw (%1$s), which is not higher than %2$s's call (%3$s). You have to bluff your way out by calling something higher! You can use the auto button for a minimal bluff."),
    MOVE_SIX_OUT("You have believed %1$s's call, and now you need to throw at least one dice to hopefully throw higher than %2$s. Luckily there is already a six! Tap on it to move it out (and keep it)."), //you can't throw higher than six anyway. Now tap on the cup to close it and shake your phone to throw the other dices."),
    ORDERING_PLAYERS("When you play the game with more than two players, the order of players becomes important. Swap players in the desired order with the up and down buttons. Put the player(A) that you want to start the game on top. Put the player(B) that sits next to player A as second. Player(C) that sits next to player B third and so on."),
    PLAYER_EXPLANATION("This app is a mobile version of a dice game we call \"Bluff Poker\". It is played with 2-10 players using a cup and three dices. You need one phone and one or more friends. Pass the phone to the next player every turn. Before you start you first have to enter the player names."),
    RETHROW_ALL_DICES("You have believed %1$s's call, and now you need to throw again, hopefully higher than %2$s. Tap the cup to close it and shake the phone to throw again.");

    @Getter
    private final String message;

    TutorialMessage(String message) {
        this.message = message;
    }
}
