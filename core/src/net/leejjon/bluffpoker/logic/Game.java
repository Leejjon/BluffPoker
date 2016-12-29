package net.leejjon.bluffpoker.logic;

import com.badlogic.gdx.audio.Sound;
import net.leejjon.bluffpoker.actions.LiftCupAction;
import net.leejjon.bluffpoker.actors.Cup;
import net.leejjon.bluffpoker.actors.Dice;
import net.leejjon.bluffpoker.interfaces.Throwable;
import net.leejjon.bluffpoker.listener.CupListener;
import net.leejjon.bluffpoker.listener.DiceListener;
import net.leejjon.bluffpoker.listener.UserInterface;
import net.leejjon.bluffpoker.stages.GameInputInterface;

import java.util.List;

public class Game implements GameInputInterface, Throwable {
    private UserInterface userInterface;
    private Settings settings;
    private List<String> originalPlayers;
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

    private boolean firstThrowSinceDeath = true;
    private boolean stillHasToThrow = true;
    private boolean allowedToBelieveOrNotBelieve = false;
    private boolean canViewOwnThrow = false;
    private boolean allowedToCall = false;
    private boolean believed666 = false;
    private boolean blindPass = false;

    public Game(Cup cup, Dice leftDice, Dice middleDice, Dice rightDice, Sound diceRoll, UserInterface userInterface) {
        this.cup = cup;
        this.leftDice = leftDice;
        this.middleDice = middleDice;
        this.rightDice = rightDice;
        this.diceRoll = diceRoll;
        this.userInterface = userInterface;

        cup.addListener(new CupListener(this));
        leftDice.addListener(new DiceListener(leftDice, this));
        middleDice.addListener(new DiceListener(middleDice, this));
        rightDice.addListener(new DiceListener(rightDice, this));
    }

    private void setGameStatusBooleans() {
        stillHasToThrow = true;
        allowedToBelieveOrNotBelieve = false;
        canViewOwnThrow = false;
        allowedToCall = false;
        believed666 = false;
        firstThrowSinceDeath = true;
        latestCall = null;
        blindPass = true;
    }

    private void constructPlayers() {
        players = new Player[originalPlayers.size()];
        for (int i = 0; i < originalPlayers.size(); i++) {
            players[i] = new Player(originalPlayers.get(i), settings.getNumberOfLives());
        }
        currentPlayer = players[playerIterator];
    }

    public void restart() {
        startGame(originalPlayers, settings);
    }

    public void startGame(List<String> originalPlayers, Settings settings) {
        this.settings = settings;
        this.originalPlayers = originalPlayers;
        cup.reset();
        setGameStatusBooleans();
        constructPlayers();
        userInterface.log("Shake the cup: " + currentPlayer.getName());
    }

    public void validateCall(NumberCombination newCall) throws InputValidationException {
        if (believed666) {
            if (newCall.areAllDicesEqual() && (newCall.isGreaterThan(latestCall) || latestCall.equals(NumberCombination.MAX))) {
                call(newCall);
            } else {
                throw new InputValidationException("Call three the identical numbers higher than " + latestCall);
            }
        } else {
            if (latestCall == null || newCall.isGreaterThan(latestCall)) {
                call(newCall);
            } else {
                throw new InputValidationException("Your call must be higher than: " + latestCall);
            }
        }
    }

    private void call(NumberCombination newCall) {
        if (cup.isWatchingOwnThrow()) {
            cup.doneWatchingOwnThrow();
        }

        boolean wereThereAnyDicesUnderTheCup = leftDice.isUnderCup() || middleDice.isUnderCup() || rightDice.isUnderCup();

        String addBlindToMessage = "";
        // Passing an empty cup doesn't count as a blind pass.
        if (blindPass && wereThereAnyDicesUnderTheCup) {
            addBlindToMessage = " (blind)";
        }

        firstThrowSinceDeath = false;
        allowedToCall = false;
        canViewOwnThrow = false;
        allowedToBelieveOrNotBelieve = true;
        blindPass = false;

        cup.unlock();

        latestCall = newCall;
        userInterface.log(currentPlayer.getName() + " called " + newCall + addBlindToMessage);
        userInterface.log(getMessageToTellNextUserToBelieveOrNot());
    }

    private String getMessageToTellNextUserToBelieveOrNot() {
        Player nextPlayer;

        int localPlayerIterator = playerIterator + 1;

        do {
            // If the localPlayerIterator runs out of the arrays bounds, we reset it to 0.
            if (localPlayerIterator == players.length) {
                localPlayerIterator = 0;
            }

            nextPlayer = players[localPlayerIterator];

            localPlayerIterator++;
        } while (nextPlayer.isDead());

        return "Believe it or not, " + nextPlayer.getName();
    }

    private void generateRandomDices() {
        leftDice.throwDice();
        middleDice.throwDice();
        rightDice.throwDice();
    }

    @Override
    public void tapCup() {
        if (!cup.isMoving()) {
            if (allowedToBelieveOrNotBelieve) {
                if (cup.isBelieving()) {
                    cup.doneBelieving();
                    allowedToBelieveOrNotBelieve = false;
                    canViewOwnThrow = true;
                } else {
                    // Start next turn.
                    nextPlayer();
                    if (latestCall.equals(new NumberCombination(6,6,6, false))) {
                        believed666 = true;
                        leftDice.putBackUnderCup();
                        middleDice.putBackUnderCup();
                        rightDice.putBackUnderCup();
                        userInterface.log(currentPlayer.getName() + " believed 666!");
                        userInterface.log("Throw three of the same numbers in one throw!");
                    } else {
                        userInterface.log(currentPlayer.getName() + " believed the call");
                    }
                    stillHasToThrow = true;

                    if (!leftDice.isUnderCup() && leftDice.getDiceValue() == 6) {
                        leftDice.lock();
                    }
                    if (!middleDice.isUnderCup() && middleDice.getDiceValue() == 6) {
                        middleDice.lock();
                    }
                    if (!rightDice.isUnderCup() && rightDice.getDiceValue() == 6) {
                        rightDice.lock();
                    }

                    cup.believe();
                    blindPass = false;
                }
            } else if (canViewOwnThrow) {
                if (cup.isWatchingOwnThrow()) {
                    cup.doneWatchingOwnThrow();
                } else {
                    cup.watchOwnThrow();
                    blindPass = false;
                }
            }
        }
    }

    @Override
    public void swipeCupUp() {
        // Obviously, you can not "not believe" something after you've first believed it, or if you have just made a throw yourself.
        if (!cup.isBelieving() && !cup.isWatchingOwnThrow() && allowedToBelieveOrNotBelieve) {
            cup.addAction(new LiftCupAction());

            if (believed666) {
                // If the latestCall is smaller or equal to the throw and the throw consists of three identical dices, the player who swiped up loses a life.
                if (!latestCall.isGreaterThan(getNumberCombinationFromDices()) && getNumberCombinationFromDices().areAllDicesEqual()) {
                    nextPlayer();
                }
            } else {
                // If the one who did not believed loses, it becomes his turn (the one who made the call was still the currentPlayer).
                if (!latestCall.isGreaterThan(getNumberCombinationFromDices())) {
                    nextPlayer();
                }
            }

            currentPlayer.loseLife(canUseBok());
            firstThrowSinceDeath = true;
            blindPass = true;
            // TODO: make sure all people on the bok die when the shared bok is allowed.

            // Detect if the current player jumped on the block and check if we should not allow other players to get on the bok too.
            if (bokAvailable && !settings.isAllowSharedBok() && currentPlayer.isRidingOnTheBok()) {
                userInterface.log(currentPlayer.getName() + " is riding on the bok");
                bokAvailable = false;
            }

            if (currentPlayer.isDead()) {
                userInterface.log(currentPlayer.getName() + " has no more lives left");
                if (!nextPlayer()) {
                    Player winner = getWinner();
                    userInterface.log(winner.getName() + " has won the game!");
                    userInterface.finishGame(winner.getName());
                    return;
                }

            } else {
                userInterface.log(currentPlayer.getName() + " lost a life and has " + currentPlayer.getLives() + " left");
            }

            // TODO: Make sure the following code is being executed after the LiftCupAction...
            userInterface.resetCall();

            // The cup should not be locked at this point.
            leftDice.reset();
            middleDice.reset();
            rightDice.reset();

            latestCall = null;
            allowedToBelieveOrNotBelieve = false;
            canViewOwnThrow = false;
            believed666 = false;
            stillHasToThrow = true;

            userInterface.log("Shake the cup: " + currentPlayer.getName());
        }
    }

    @Override
    public boolean longTapOnCup() {
        if (!cup.isMoving()) {
            if (allowedToBelieveOrNotBelieve) {
                // Start next turn.
                nextPlayer();
                cup.lock();

                userInterface.log(currentPlayer.getName() + " believed the call (blind)");

                allowedToBelieveOrNotBelieve = false;
                allowedToCall = true;
                stillHasToThrow = false;
                blindPass = true;
                userInterface.enableCallUserInterface();
                // canViewOwnThrow is already false, so let's keep it false.
                //canViewOwnThrow = false;
                return true;
            } else if (!allowedToBelieveOrNotBelieve && blindPass && !stillHasToThrow && !firstThrowSinceDeath) {
                cup.unlock();
                stillHasToThrow = true;
                userInterface.disableCallUserInterface();
                allowedToCall = false;
                canViewOwnThrow = true;

                if (!leftDice.isUnderCup()) {
                    leftDice.lock();
                }
                if (!middleDice.isUnderCup()) {
                    middleDice.lock();
                }
                if (!rightDice.isUnderCup()) {
                    rightDice.lock();
                }
                return true;
            } else {
                // Very important to use stillHasToThrow and not isAllowedToThrow().
                if (stillHasToThrow && !firstThrowSinceDeath) {
                    if (cup.isLocked()) {
                        cup.unlock();
                        if (leftDice.isUnderCup()) {
                            leftDice.unlock();
                        }
                        if (middleDice.isUnderCup()) {
                            middleDice.unlock();
                        }
                        if (rightDice.isUnderCup()) {
                            rightDice.unlock();
                        }
                    } else {
                        cup.lock();
                        if (leftDice.isUnderCup()) {
                            leftDice.lock();
                        }
                        if (middleDice.isUnderCup()) {
                            middleDice.lock();
                        }
                        if (rightDice.isUnderCup()) {
                            rightDice.lock();
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Watch out, this is a recursive function.
     * @return A boolean if there is a next player.
     */
    private boolean nextPlayer() {
        Player winner = getWinner();
        if (winner != null) {
            return false;
        }

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

        // There is more than one player left
        return true;
    }

    private Player getWinner() {
        int numberOfPlayersStillAlive = 0;

        // Check if there are still more than two players alive.
        int indexOfLastLivingPlayer = 0;
        for (int i = 0; i < players.length; i++) {
            Player p = players[i];
            if (!p.isDead()) {
                numberOfPlayersStillAlive++;
                indexOfLastLivingPlayer = i;
            }
        }

        if (numberOfPlayersStillAlive < 2) {
            // Set the winning player as the one to start next game.
            playerIterator = indexOfLastLivingPlayer;

            return players[playerIterator];
        } else {
            return null;
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

    public void throwDices() {
        cup.reset();
        diceRoll.play(1.0f);
        generateRandomDices();
        stillHasToThrow = false;
        canViewOwnThrow = true;
        allowedToCall = true;
        cup.unlock();
        leftDice.unlock();
        middleDice.unlock();
        rightDice.unlock();
        userInterface.log("Now enter your call ...");
    }

    /**
     * @return A NumberCombination object based on the values of the dices.
     */
    private NumberCombination getNumberCombinationFromDices() {
        return new NumberCombination(leftDice.getDiceValue(), middleDice.getDiceValue(), rightDice.getDiceValue(), true);
    }

    public boolean isAllowedToCall() {
        return allowedToCall;
    }

    public boolean hasBelieved666() {
        return believed666;
    }

    public NumberCombination getLatestCall() {
        return latestCall;
    }

    public void resetPlayerIterator() {
        playerIterator = 0;
    }

    public boolean isAllowedToThrow() {
        if (stillHasToThrow && !cup.isMoving() && !cup.isBelieving() && !cup.isWatchingOwnThrow()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean stillHasToThrow() {
        return stillHasToThrow;
    }
}
