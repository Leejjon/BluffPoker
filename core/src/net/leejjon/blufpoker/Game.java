package net.leejjon.blufpoker;

import com.badlogic.gdx.audio.Sound;
import net.leejjon.blufpoker.actions.LiftCupAction;
import net.leejjon.blufpoker.actors.Cup;
import net.leejjon.blufpoker.actors.Dice;
import net.leejjon.blufpoker.listener.CupListener;
import net.leejjon.blufpoker.listener.LogListener;
import net.leejjon.blufpoker.stages.GameInputInterface;

import java.util.List;

/**
 * Created by Leejjon on 3-10-2015.
 */
public class Game implements GameInputInterface {
    private LogListener logger;
    private Settings settings;
    private Player[] players;
    private int playerIterator = 0;

    private Cup cup;
    private Dice leftDice;
    private Dice middleDice;
    private Dice rightDice;
    private Sound diceRoll;

    private boolean bokAvailable = true;

    /**
     * The player that has the turn.
     */
    private Player currentPlayer;
    private NumberCombination latestCall = null;

    private boolean isAllowedToThrow = true;
    private boolean isAllowedToBelieveOrNotBelieve = false;
    private boolean canViewOwnThrow = false;

    public Game(List<String> playerNames, Settings settings, Cup cup, Dice leftDice, Dice middleDice, Dice rightDice, Sound diceRoll, LogListener logger) {
        this.settings = settings;
        this.cup = cup;
        this.leftDice = leftDice;
        this.middleDice = middleDice;
        this.rightDice = rightDice;
        this.diceRoll = diceRoll;
        this.logger = logger;

        cup.addListener(new CupListener(this));

        players = new Player[playerNames.size()];
        for (int i = 0; i < playerNames.size(); i++) {
            players[i] = new Player(playerNames.get(i), settings.getNumberOfLives());
        }

        currentPlayer = players[playerIterator];
    }

    public void startGame() {
        logger.log("Shake the cup: " + currentPlayer.getName());
    }

    public void call(NumberCombination newCall) throws InputValidationException {
        if (latestCall == null || newCall.isGreaterThan(latestCall)) {
            canViewOwnThrow = false;
            isAllowedToBelieveOrNotBelieve = true;
            latestCall = newCall;
            logger.log(currentPlayer.getName() + " called " + newCall);
            logger.log(getMessageToTellNextUserToBelieveOrNot());
        } else {
            throw new InputValidationException("Your call must be higher than: " + latestCall);
        }
    }

    private String getMessageToTellNextUserToBelieveOrNot() {
        Player nextPlayer;
        if (playerIterator + 1 < players.length) {
            nextPlayer = players[playerIterator + 1];
        } else {
            nextPlayer = players[0];
        }
        return "Believe it or not, " + nextPlayer.getName();
    }

    public boolean isAllowedToThrow() {
        return isAllowedToThrow;
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
                if (cup.isBelieving()) {
                    cup.doneBelieving();
                    isAllowedToBelieveOrNotBelieve = false;
                    isAllowedToThrow = true;
                } else {
                    cup.believe();
                    // TODO: Start of next turn
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

            // If the one who did not believed loses, it becomes his turn (the one who made the call was still the currentPlayer).
            if (!latestCall.isGreaterThan(getNumberCombinationFromDices())) {
                nextPlayer();
            }

            currentPlayer.loseLife(canUseBok());

            // Detect if the current player jumped on the block and check if we should not allow other players to get on the bok too.
            if (!settings.isAllowSharedBok() && currentPlayer.isRidingOnTheBok()) {
                bokAvailable = false;
            }

            if (currentPlayer.isDead()) {
                logger.log(currentPlayer.getName() + " has no more lives left");
                nextPlayer();

            } else {
                logger.log(currentPlayer.getName() + " lost a life and has " + currentPlayer.getLives() +" left");
            }

            // TODO: Make sure the following code is being executed after the LiftCupAction...

            latestCall = null;
            isAllowedToBelieveOrNotBelieve = false;
            canViewOwnThrow = false;
            isAllowedToThrow = true;

            logger.log("Shake the cup: " + currentPlayer.getName());
        }
    }

    /**
     * Watch out, recursive stuff.
     */
    private void nextPlayer() {
        // TODO: Check if there is a winner.

        if (playerIterator+1 < players.length) {
            playerIterator++;
        } else {
            playerIterator = 0;
        }

        if (players[playerIterator].isDead()) {
            nextPlayer();
        } else {
            currentPlayer = players[playerIterator];
        }
    }

    private boolean canUseBok() {
        if (settings.isAllowBok()) {
            if (bokAvailable) {
                return true;
            } else {
                if (settings.isAllowSharedBok()) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
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

    /**
     * @return A NumberCombination object based on the values of the dices.
     */
    private NumberCombination getNumberCombinationFromDices() {
        return new NumberCombination(leftDice.getDiceValue(), middleDice.getDiceValue(), rightDice.getDiceValue());
    }
}
