package net.leejjon.blufpoker;

import com.badlogic.gdx.audio.Sound;
import net.leejjon.blufpoker.actions.LiftCupAction;
import net.leejjon.blufpoker.actors.Cup;
import net.leejjon.blufpoker.actors.Dice;
import net.leejjon.blufpoker.listener.CupListener;
import net.leejjon.blufpoker.stages.GameInputInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Leejjon on 3-10-2015.
 */
public class Game implements GameInputInterface {
    private Settings settings;
    private List<Player> players = new ArrayList<>();
    private Iterator<Player> playerIterator;

    private Cup cup;
    private Dice leftDice;
    private Dice middleDice;
    private Dice rightDice;
    private Sound diceRoll;

    /**
     * The player that has the turn.
     */
    private Player currentPlayer;

    private boolean isAllowedToThrow = true;
    private boolean isAllowedToBelieveOrNotBelieve = false;
    private boolean canViewOwnThrow = false;

    public Game(List<String> playerNames, Settings settings, Cup cup, Dice leftDice, Dice middleDice, Dice rightDice, Sound diceRoll) {
        this.settings = settings;
        this.cup = cup;
        this.leftDice = leftDice;
        this.middleDice = middleDice;
        this.rightDice = rightDice;
        this.diceRoll = diceRoll;


        cup.addListener(new CupListener(this));

        for (String name : playerNames) {
            players.add(new Player(name, settings.getNumberOfLives()));
        }

        playerIterator = players.iterator();
        currentPlayer = playerIterator.next();
    }

    public String startGame() {
        return "Shake the cup: " + currentPlayer.getName();
    }

    public boolean isAllowedToThrow() {
        return isAllowedToThrow;
    }

    public boolean isAllowedToBelieveOrNotBelieve() {
        return isAllowedToBelieveOrNotBelieve;
    }

    private void generateRandomDices() {
        leftDice.throwDice();
        middleDice.throwDice();
        rightDice.throwDice();
    }

    @Override
    public void tapCup() {
        if (!cup.isMoving()) {
            if (isAllowedToBelieveOrNotBelieve) {
                // Once you've swiped up, you can no longer believe.
                if (cup.isBelieving()) {
                    cup.doneBelieving();
                } else {
                    cup.believe();
                }
            } else if (canViewOwnThrow) {
                if (cup.isWatchingOwnThrow()) {
                    cup.doneWatchingOwnThrow();
                } else {
                    cup.watchOwnThrow();
                }
            }
        }
    }

    @Override
    public void swipeCupUp() {
        // Obviously, you can not "not believe" something after you've first believed it, or if you have just made a throw yourself.
        if (!cup.isBelieving() && !cup.isWatchingOwnThrow() && isAllowedToBelieveOrNotBelieve) {
            cup.addAction(new LiftCupAction());
            // TODO: Rest of what needs to happen after not believing, like calculating who lost a live.
        }
    }

    @Override
    public String throwDicesInCup() {
        cup.reset();
        diceRoll.play(1.0f);
        generateRandomDices();
        isAllowedToThrow = false;
        canViewOwnThrow = true;
        return "Now enter your call ...";
    }
}
