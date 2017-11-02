package net.leejjon.bluffpoker.logic;

import com.badlogic.gdx.audio.Sound;
import net.leejjon.bluffpoker.actions.LiftCupAction;
import net.leejjon.bluffpoker.actors.Cup;
import net.leejjon.bluffpoker.actors.Dice;
import net.leejjon.bluffpoker.enums.TutorialMessage;
import net.leejjon.bluffpoker.interfaces.GameStatusInterface;
import net.leejjon.bluffpoker.listener.CupListener;
import net.leejjon.bluffpoker.listener.DiceListener;
import net.leejjon.bluffpoker.interfaces.UserInterface;
import net.leejjon.bluffpoker.interfaces.GameInputInterface;
import net.leejjon.bluffpoker.state.Settings;

import java.util.List;

public class Game implements GameInputInterface, GameStatusInterface {
    private UserInterface userInterface;
    private List<String> originalPlayers;
    private Player[] players;

    private int playerIterator = 0;

    private Cup cup;
    private Dice leftDice;
    private Dice middleDice;
    private Dice rightDice;
    private Sound diceRoll;

    private Settings settings;

    private boolean bokAvailable = true;

    /**
     * The player that has the turn.
     */
    private Player currentPlayer;

    private Call latestCall = null;

    //
    private boolean firstThrowSinceDeath = true;
    private boolean hasToThrow = true;
    private boolean hasThrown = false;
    private boolean allowedToBelieveOrNotBelieve = false;
    private boolean canViewOwnThrow = false;
    private boolean allowedToCall = false;
    private boolean believed666 = false;
    private boolean blindPass = false;

    // Booleans to remember whether a tutorial message already has been shown to avoid duplicate message spamming.
    private boolean lookAtOwnThrowMessageHasBeenShown = false;

    private static final String BELIEVE_IT_OR_NOT = "Believe it or not, %1$s";
    private static final String WATCH_OWN_THROW = "You can watch your own throw, %s";
    public static final String SHAKE_THE_CUP = "Shake the cup: %1$s";
    private static final String CALL_THREE_IDENTICAL_NUMBERS_HIGHER_THAN = "Call three the identical numbers higher than ";
    private static final String YOUR_CALL_MUST_BE_HIGHER_THAN = "Your call must be higher than: ";
    private static final String BLIND_MESSAGE = " (blind)";
    private static final String CALLED = " called ";
    private static final String BELIEVED_666 = " believed 666!";
    private static final String BELIEVED_THE_CALL = " believed the call";
    private static final String BELIEVED_THE_CALL_BLIND = " believed the call (blind)";
    private static final String THROW_THREE_OF_THE_SAME_NUMBERS_IN_ONE_THROW = "Throw three of the same numbers in one throw!";
    private static final String NOW_ENTER_YOUR_CALL = "Now enter your call ...";
    private static final String NOW_ENTER_YOUR_CALL_OR_THROW = "Now enter your call or throw ...";
    private static final String RIDING_ON_THE_BOK = " is riding on the bok";
    private static final String WANTED_TO_PEEK_AFTER_ALL = " wanted to peek after all";
    private static final String LOST_A_LIFE = "%1$s lost a life and has %2$d left";
    private static final String WON_THE_GAME = "%1$s has won the game!";
    private static final String HAS_NO_MORE_LIVES_LEFT = "%1$s has no more lives left";


    public Game(Cup cup, Dice leftDice, Dice middleDice, Dice rightDice, Sound diceRoll, UserInterface userInterface, Settings settings) {
        this.cup = cup;
        this.leftDice = leftDice;
        this.middleDice = middleDice;
        this.rightDice = rightDice;
        this.diceRoll = diceRoll;
        this.userInterface = userInterface;
        this.settings = settings;

        cup.addListener(new CupListener(this));
        leftDice.addListener(new DiceListener(leftDice, this, userInterface));
        middleDice.addListener(new DiceListener(middleDice, this, userInterface));
        rightDice.addListener(new DiceListener(rightDice, this, userInterface));
    }

    private void setGameStatusBooleans() {
        hasToThrow = true;
        allowedToBelieveOrNotBelieve = false;
        canViewOwnThrow = false;
        allowedToCall = false;
        believed666 = false;
        firstThrowSinceDeath = true;
        latestCall = null;
        blindPass = false;
    }

    private void constructPlayers() {
        players = new Player[originalPlayers.size()];
        for (int i = 0; i < originalPlayers.size(); i++) {
            players[i] = new Player(originalPlayers.get(i), settings.getNumberOfLives());
        }
        currentPlayer = players[playerIterator];
    }

    public void restart() {
        startGame(originalPlayers);
    }

    public void startGame(List<String> originalPlayers) {
        this.originalPlayers = originalPlayers;
        cup.reset();
        setGameStatusBooleans();
        constructPlayers();
        userInterface.log(String.format(SHAKE_THE_CUP, currentPlayer.getName()));

        userInterface.showTutorialMessage(TutorialMessage.GAME_START, currentPlayer.getName());
    }

    public void validateCall(NumberCombination newCall) throws InputValidationException {
        if (believed666) {
            if (newCall.areAllDicesEqual() && (newCall.isGreaterThan(latestCall.getNumberCombination()) || latestCall.getNumberCombination().equals(NumberCombination.MAX))) {
                call(newCall);
            } else {
                throw new InputValidationException(CALL_THREE_IDENTICAL_NUMBERS_HIGHER_THAN + latestCall.getNumberCombination());
            }
        } else {
            if (latestCall == null || newCall.isGreaterThan(latestCall.getNumberCombination())) {
                call(newCall);
            } else {
                throw new InputValidationException(YOUR_CALL_MUST_BE_HIGHER_THAN + latestCall.getNumberCombination());
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
            addBlindToMessage = BLIND_MESSAGE;
            blindPass = false;
        }

        firstThrowSinceDeath = false;
        allowedToCall = false;
        canViewOwnThrow = false;
        allowedToBelieveOrNotBelieve = true;

        cup.unlock();

        latestCall = new Call(currentPlayer, newCall);
        userInterface.log(currentPlayer.getName() + CALLED + newCall + addBlindToMessage);
        userInterface.log(getMessageToTellNextUserToBelieveOrNot());

        userInterface.showTutorialMessage(TutorialMessage.BELIEVE_OR_NOT_BELIEVE,
                getLatestCall().getPlayer().getName(),
                getNextPlayer().getName(),
                getLatestCall().getNumberCombination().toString());
    }

    private String getMessageToTellNextUserToBelieveOrNot() {
        return String.format(BELIEVE_IT_OR_NOT, getNextPlayer().getName());
    }

    private Player getNextPlayer() {
        Player nextPlayer;

        int localPlayerIterator = playerIterator + 1;

        // Fingers crossed: At this point (during placing a call) not all players should be dead.
        do {
            // If the localPlayerIterator runs out of the arrays bounds, we reset it to 0.
            if (localPlayerIterator == players.length) {
                localPlayerIterator = 0;
            }

            nextPlayer = players[localPlayerIterator];

            localPlayerIterator++;
        } while (nextPlayer.isDead());
        return nextPlayer;
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
                    if (latestCall.getNumberCombination().equals(NumberCombination.MAX)) {
                        // TODO: Do we need to make the dices visible here?
                        believed666 = true;

                        // TODO: Do we really need to put them back under the cup?
                        leftDice.putBackUnderCup();
                        middleDice.putBackUnderCup();
                        rightDice.putBackUnderCup();
                        userInterface.log(currentPlayer.getName() + BELIEVED_666);
                        userInterface.log(THROW_THREE_OF_THE_SAME_NUMBERS_IN_ONE_THROW);
                    } else {
                        userInterface.log(currentPlayer.getName() + BELIEVED_THE_CALL);
                        userInterface.log("Throw at least one dice ...");

                        if (getNumberCombinationFromDices().isGreaterThan(new NumberCombination(6,0,0, true))) {
                            userInterface.showTutorialMessage(TutorialMessage.MOVE_SIX_OUT, latestCall.getPlayer().getName(), latestCall.getNumberCombination().toString());
                        } else {
                            userInterface.showTutorialMessage(TutorialMessage.RETHROW_ALL_DICES, latestCall.getPlayer().getName(), latestCall.getNumberCombination().toString());
                        }
                    }
                    hasToThrow = true;
                    hasThrown = false;

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

                    lookAtOwnThrowMessageHasBeenShown = false;
                }
            } else if (canViewOwnThrow) {
                if (cup.isWatchingOwnThrow()) {
                    cup.doneWatchingOwnThrow();
                } else {
                    cup.watchOwnThrow();
                    blindPass = false;
                    if (!lookAtOwnThrowMessageHasBeenShown && hasThrown) {
                        if (latestCall == null) {
                            userInterface.showTutorialMessage(TutorialMessage.LOOKING_AT_OWN_THROW_FIRST_TURN_SINCE_DEATH,
                                    String.valueOf(leftDice.getDiceValue()),
                                    String.valueOf(middleDice.getDiceValue()),
                                    String.valueOf(rightDice.getDiceValue()),
                                    getNumberCombinationFromDices().toString());
                        } else {
                            if (getNumberCombinationFromDices().isGreaterThan(latestCall.getNumberCombination())) {
                                userInterface.showTutorialMessage(TutorialMessage.LOOK_AT_OWN_THROW_THAT_IS_HIGHER,
                                        getNumberCombinationFromDices().toString(),
                                        latestCall.getPlayer().getName(),
                                        latestCall.getNumberCombination().toString());
                            } else {
                                userInterface.showTutorialMessage(TutorialMessage.LOOK_AT_OWN_THROW_THAT_IS_LOWER,
                                        getNumberCombinationFromDices().toString(),
                                        latestCall.getPlayer().getName(),
                                        latestCall.getNumberCombination().toString());
                            }
                        }
                        lookAtOwnThrowMessageHasBeenShown = true;
                    }
                }
            }
        }
    }

    @Override
    public void swipeCupUp() {
        // Obviously, you can not "not believe" something after you've first believed it, or if you have just made a throw yourself.
        if (!cup.isBelieving() && !cup.isWatchingOwnThrow() && allowedToBelieveOrNotBelieve) {
            cup.addAction(new LiftCupAction());

            // Variables for the tutorial mode.
            boolean wasBluffing = true;
            final String callingPlayer = currentPlayer.getName();
            final NumberCombination whatWasCalled = latestCall.getNumberCombination();

            if (believed666) {
                // If the latestCall is smaller or equal to the throw and the throw consists of three identical dices, the player
                // who swiped up loses a life and it becomes his turn.
                if (!latestCall.getNumberCombination().isGreaterThan(getNumberCombinationFromDices()) && getNumberCombinationFromDices().areAllDicesEqual()) {
                    nextPlayer();
                    wasBluffing = false;
                }
            } else {
                // If the one who did not believed loses, it becomes his turn (the one who made the call was still the currentPlayer).
                if (!latestCall.getNumberCombination().isGreaterThan(getNumberCombinationFromDices())) {
                    nextPlayer();
                    wasBluffing = false;
                }
            }

            currentPlayer.loseLife(canUseBok());
            firstThrowSinceDeath = true;
            blindPass = true;
            // TODO: make sure all people on the bok die when the shared bok is allowed.

            // Detect if the current player jumped on the block and check if we should not allow other players to get on the bok too.
            if (bokAvailable && !settings.isAllowSharedBok() && currentPlayer.isRidingOnTheBok()) {
                userInterface.log(currentPlayer.getName() + RIDING_ON_THE_BOK);
                bokAvailable = false;
            }

            if (currentPlayer.isDead()) {
                userInterface.log(String.format(HAS_NO_MORE_LIVES_LEFT, currentPlayer.getName()));

                if (!nextPlayer()) {
                    Player winner = getWinner();
                    userInterface.log(String.format(WON_THE_GAME, winner.getName()));
                    userInterface.finishGame(winner.getName());
                    return;
                }
            } else {
                userInterface.log(String.format(LOST_A_LIFE, currentPlayer.getName(), currentPlayer.getLives()));
            }

            // The cup should not be locked at this point.
            leftDice.reset();
            middleDice.reset();
            rightDice.reset();

            latestCall = null;
            allowedToBelieveOrNotBelieve = false;
            canViewOwnThrow = false;
            believed666 = false;
            hasToThrow = true;
            hasThrown = false;

            userInterface.log(String.format(SHAKE_THE_CUP, currentPlayer.getName()));

            lookAtOwnThrowMessageHasBeenShown = false;
            if (wasBluffing) {
                userInterface.showTutorialMessage(TutorialMessage.DID_NOT_BELIEVE_BLUFF, callingPlayer, whatWasCalled.toString());
            } else {
                userInterface.showTutorialMessage(TutorialMessage.DID_NOT_BELIEVE_THRUTH, callingPlayer, whatWasCalled.toString());
            }
        }
    }

    /**
     * @return If long tapping isn't allowed in this game phase, we return false so it doesn't blocks other events like
     * swipe or tapping. If it is allowed, we return true to avoid any swipes/taps being activated while attempting a
     * long tap.
     */
    @Override
    public boolean longTapOnCup() {
        if (!cup.isMoving()) {
            // If the player wants to blindly believe another players call and pass it on.
            if (!blindPass && !hasToThrow && allowedToBelieveOrNotBelieve) {
                // Start next turn. We expect the user to want to do a blind pas (while increasing his call).
                nextPlayer();
                cup.lock();

                // Lock the dices in case they are lying outside of the cup.
                leftDice.lock();
                middleDice.lock();
                rightDice.lock();

                userInterface.log(currentPlayer.getName() + BELIEVED_THE_CALL_BLIND);

                allowedToBelieveOrNotBelieve = false;
                allowedToCall = true;
                hasToThrow = false;
                hasThrown = false;
                blindPass = true;
                userInterface.enableCallUserInterface();
                userInterface.log(NOW_ENTER_YOUR_CALL_OR_THROW);
                // canViewOwnThrow is already false, so let's keep it false.
                //canViewOwnThrow = false;
                return true;
                // Blindly pass your own throw.
            } else if (blindPass && hasThrown && firstThrowSinceDeath && !allowedToBelieveOrNotBelieve) {
                cup.lock();

                // Leave blindPass true on purpose.

                allowedToCall = true;
                canViewOwnThrow = false;

                userInterface.enableCallUserInterface();
                userInterface.log(NOW_ENTER_YOUR_CALL);
                // Unlocking halfway the turn.
            } else if (blindPass && !hasToThrow && !firstThrowSinceDeath) {
                cup.unlock();
                cup.believe();

                // Don't set blindPass to false, the player simply has to throw again and can call without watching and then it will be set to blind again.
                hasToThrow = true;
                userInterface.disableCallUserInterface();
                userInterface.log(currentPlayer.getName() + WANTED_TO_PEEK_AFTER_ALL);
                allowedToCall = false;
                canViewOwnThrow = true;

                if (!leftDice.isUnderCup()) {
                    leftDice.lock();
                } else {
                    leftDice.unlock();
                }

                if (!middleDice.isUnderCup()) {
                    middleDice.lock();
                } else {
                    middleDice.unlock();
                }

                if (!rightDice.isUnderCup()) {
                    rightDice.lock();
                } else {
                    rightDice.unlock();
                }
                return true;
                // Just locking / unlocking.
            } else if (!blindPass && hasToThrow && !firstThrowSinceDeath) {
                // Very important to use hasToThrow and not isAllowedToThrow().
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
            } else {
                throw new IllegalStateException(String.format("Illegal appState detected: blindPass=%b, hasThrown=%b, hasToThrow=%b, firstThrowSinceDeath=%b, allowedToBelieveOrNotBelieve=%b", blindPass, hasThrown, hasToThrow, firstThrowSinceDeath, allowedToBelieveOrNotBelieve));
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

        final Dice.ThrowResult leftResult = leftDice.throwDice();
        final Dice.ThrowResult middleResult = middleDice.throwDice();
        final Dice.ThrowResult rightResult = rightDice.throwDice();

        hasToThrow = false;
        hasThrown = true;
        canViewOwnThrow = true;
        allowedToCall = true;

        if (leftResult == Dice.ThrowResult.UNDER_CUP || middleResult == Dice.ThrowResult.UNDER_CUP || rightResult == Dice.ThrowResult.UNDER_CUP) {
            blindPass = true; // Everytime you throw a dice under the cup, it starts out as a blind pass.
        }

        cup.unlock();
        leftDice.unlock();
        middleDice.unlock();
        rightDice.unlock();

        if (firstThrowSinceDeath) {
            userInterface.resetCall();
            userInterface.log(String.format(WATCH_OWN_THROW, currentPlayer.getName()));
            userInterface.showTutorialMessage(TutorialMessage.FIRST_THROWN_SINCE_DEATH);
        } else {
            userInterface.log(NOW_ENTER_YOUR_CALL);
        }
    }

    /**
     * @return A NumberCombination object based on the values of the dices.
     */
    public NumberCombination getNumberCombinationFromDices() {
        return new NumberCombination(leftDice.getDiceValue(), middleDice.getDiceValue(), rightDice.getDiceValue(), true);
    }

    public boolean isAllowedToCall() {
        return allowedToCall;
    }

    public boolean hasBelieved666() {
        return believed666;
    }

    public Call getLatestCall() {
        return latestCall;
    }

    public void resetPlayerIterator() {
        playerIterator = 0;
    }

    @Override
    public boolean isAllowedToThrow() {
        if ((hasToThrow || (blindPass && !hasThrown)) && !cup.isMoving() && !cup.isBelieving() && !cup.isWatchingOwnThrow()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isAllowedToLock() {
        if ((hasToThrow || (blindPass && !hasThrown)) && !cup.isMoving()) {
            return true;
        } else {
            return false;
        }
    }
}
