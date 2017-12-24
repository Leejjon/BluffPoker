package net.leejjon.bluffpoker.logic;

import com.badlogic.gdx.audio.Sound;
import net.leejjon.bluffpoker.actions.LiftCupAction;
import net.leejjon.bluffpoker.actors.DiceActor;
import net.leejjon.bluffpoker.enums.TutorialMessage;
import net.leejjon.bluffpoker.listener.CupListener;
import net.leejjon.bluffpoker.listener.DiceListener;
import net.leejjon.bluffpoker.interfaces.UserInterface;
import net.leejjon.bluffpoker.interfaces.GameInputInterface;
import net.leejjon.bluffpoker.state.GameState;
import net.leejjon.bluffpoker.state.SettingsState;

import java.util.ArrayList;

public class Game implements GameInputInterface {
    private DiceActor leftDiceActor;
    private DiceActor middleDiceActor;
    private DiceActor rightDiceActor;
    private Sound diceRoll;

    private SettingsState settingsState;

    private UserInterface userInterface;

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


    public Game(DiceActor leftDiceActor, DiceActor middleDiceActor, DiceActor rightDiceActor, Sound diceRoll, UserInterface userInterface) {
        this.leftDiceActor = leftDiceActor;
        this.middleDiceActor = middleDiceActor;
        this.rightDiceActor = rightDiceActor;
        this.diceRoll = diceRoll;
        this.userInterface = userInterface;
        this.settingsState = SettingsState.getInstance();

        state().getCup().getCupActor().addListener(new CupListener(this));
        leftDiceActor.addListener(new DiceListener(leftDiceActor, userInterface));
        middleDiceActor.addListener(new DiceListener(middleDiceActor, userInterface));
        rightDiceActor.addListener(new DiceListener(rightDiceActor, userInterface));
    }

    public void startGame(ArrayList<String> originalPlayers) {
        state().getCup().getCupActor().reset();
        state().constructPlayers(originalPlayers, settingsState.getNumberOfLives());
        state().logGameConsoleMessage(String.format(SHAKE_THE_CUP, state().getCurrentPlayer().getName()));
        userInterface.showTutorialMessage(TutorialMessage.GAME_START, state().getCurrentPlayer().getName());
    }

    public void validateCall(NumberCombination newCall) throws InputValidationException {
        if (state().isBelieved666()) {
            if (newCall.areAllDicesEqual() && (newCall.isGreaterThan(state().getLatestCall().getNumberCombination()) || state().getLatestCall().getNumberCombination().equals(NumberCombination.MAX))) {
                call(newCall);
            } else {
                throw new InputValidationException(CALL_THREE_IDENTICAL_NUMBERS_HIGHER_THAN + state().getLatestCall().getNumberCombination());
            }
        } else {
            if (state().getLatestCall() == null || newCall.isGreaterThan(state().getLatestCall().getNumberCombination())) {
                call(newCall);
            } else {
                throw new InputValidationException(YOUR_CALL_MUST_BE_HIGHER_THAN + state().getLatestCall().getNumberCombination());
            }
        }
    }

    private void call(NumberCombination newCall) {
        if (state().getCup().isWatchingOwnThrow()) {
            state().getCup().getCupActor().doneWatchingOwnThrow();
        }

        boolean wereThereAnyDicesUnderTheCup = leftDiceActor.isUnderCup() || middleDiceActor.isUnderCup() || rightDiceActor.isUnderCup();

        String addBlindToMessage = "";
        // Passing an empty cup doesn't count as a blind pass.
        if (state().isBlindPass() && wereThereAnyDicesUnderTheCup) {
            addBlindToMessage = BLIND_MESSAGE;
            state().setBlindPass(false);
        }

        state().getCup().unlock();
        state().allowPlayerToCall(false);
        state().updateLatestCall(new Call(state().getCurrentPlayer().getName(), newCall));
        state().setCallInput(newCall.toString());

        state().logGameConsoleMessage(state().getCurrentPlayer().getName() + CALLED + newCall + addBlindToMessage);
        state().logGameConsoleMessage(getMessageToTellNextUserToBelieveOrNot());

        userInterface.showTutorialMessage(TutorialMessage.BELIEVE_OR_NOT_BELIEVE,
                state().getLatestCall().getPlayerName(),
                getNextPlayer().getName(),
                state().getLatestCall().getNumberCombination().toString());
    }

    private String getMessageToTellNextUserToBelieveOrNot() {
        return String.format(BELIEVE_IT_OR_NOT, getNextPlayer().getName());
    }

    private Player getNextPlayer() {
        Player nextPlayer;

        int localPlayerIterator = state().getPlayerIterator() + 1;

        // At this point (during placing a call) not all players should be dead.
        do {
            // If the localPlayerIterator runs out of the arrays bounds, we reset it to 0.
            if (localPlayerIterator == state().getPlayers().length) {
                localPlayerIterator = 0;
            }

            nextPlayer = state().getPlayers()[localPlayerIterator];

            localPlayerIterator++;
        } while (nextPlayer.isDead());
        return nextPlayer;
    }

    @Override
    public void tapCup() {
        if (!state().getCup().getCupActor().isMoving()) {
            if (state().isAllowedToBelieveOrNotBelieve()) {
                if (state().getCup().isBelieving()) {
                    state().getCup().getCupActor().doneBelieving();
                    state().setAllowedToBelieveOrNotBelieve(false);
                    state().setCanViewOwnThrow(true);
                } else {
                    // Start next turn.
                    nextPlayer();
                    if (state().getLatestCall().getNumberCombination().equals(NumberCombination.MAX)) {
                        believe666();
                    } else {
                        believe();
                    }
                }
            } else if (state().isCanViewOwnThrow()) {
                if (state().getCup().isWatchingOwnThrow()) {
                    state().getCup().getCupActor().doneWatchingOwnThrow();
                } else {
                    state().getCup().getCupActor().watchOwnThrow();
                    state().setBlindPass(false);
                    if (!lookAtOwnThrowMessageHasBeenShown && state().isHasThrown()) {
                        if (state().getLatestCall() == null) {
                            userInterface.showTutorialMessage(TutorialMessage.LOOKING_AT_OWN_THROW_FIRST_TURN_SINCE_DEATH,
                                    String.valueOf(leftDiceActor.getDiceValue()),
                                    String.valueOf(middleDiceActor.getDiceValue()),
                                    String.valueOf(rightDiceActor.getDiceValue()),
                                    getNumberCombinationFromDices().toString());
                        } else {
                            if (getNumberCombinationFromDices().isGreaterThan(state().getLatestCall().getNumberCombination())) {
                                userInterface.showTutorialMessage(TutorialMessage.LOOK_AT_OWN_THROW_THAT_IS_HIGHER,
                                        getNumberCombinationFromDices().toString(),
                                        state().getLatestCall().getPlayerName(),
                                        state().getLatestCall().getNumberCombination().toString());
                            } else {
                                userInterface.showTutorialMessage(TutorialMessage.LOOK_AT_OWN_THROW_THAT_IS_LOWER,
                                        getNumberCombinationFromDices().toString(),
                                        state().getLatestCall().getPlayerName(),
                                        state().getLatestCall().getNumberCombination().toString());
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
        if (!state().getCup().isBelieving() && !state().getCup().isWatchingOwnThrow() && state().isAllowedToBelieveOrNotBelieve()) {
            state().getCup().getCupActor().addAction(new LiftCupAction());

            // Variables for the tutorial mode.
            boolean wasBluffing = true;
            final String callingPlayer = state().getCurrentPlayer().getName();
            final NumberCombination whatWasCalled = state().getLatestCall().getNumberCombination();

            if (state().isBelieved666()) {
                // If the latestCall is smaller or equal to the throw and the throw consists of three identical dices, the player
                // who swiped up loses a life and it becomes his turn.
                if (!state().getLatestCall().getNumberCombination().isGreaterThan(getNumberCombinationFromDices()) && getNumberCombinationFromDices().areAllDicesEqual()) {
                    nextPlayer();
                    wasBluffing = false;
                }
            } else {
                // If the one who did not believed loses, it becomes his turn (the one who made the call was still the currentPlayer).
                if (!state().getLatestCall().getNumberCombination().isGreaterThan(getNumberCombinationFromDices())) {
                    nextPlayer();
                    wasBluffing = false;
                }
            }

            state().currentPlayerLosesLife(canUseBok());
            state().setFirstThrowSinceDeath(true);
            state().setBlindPass(true);
            // TODO: make sure all people on the bok die when the shared bok is allowed.

            // Detect if the current player jumped on the block and check if we should not allow other players to get on the bok too.
            if (state().isBokAvailable() && !settingsState.isAllowSharedBok() && state().getCurrentPlayer().isRidingOnTheBok()) {
                state().logGameConsoleMessage(state().getCurrentPlayer().getName() + RIDING_ON_THE_BOK);
                state().setBokAvailable(false);
            }

            if (state().getCurrentPlayer().isDead()) {
                state().logGameConsoleMessage(String.format(HAS_NO_MORE_LIVES_LEFT, state().getCurrentPlayer().getName()));

                if (!nextPlayer()) {
                    Player winner = getWinner();
                    state().logGameConsoleMessage(String.format(WON_THE_GAME, winner.getName()));
                    userInterface.finishGame(winner.getName());
                    return;
                }
            } else {
                state().logGameConsoleMessage(String.format(LOST_A_LIFE, state().getCurrentPlayer().getName(), state().getCurrentPlayer().getLives()));
            }

            // The cup should not be locked at this point.
            leftDiceActor.reset();
            middleDiceActor.reset();
            rightDiceActor.reset();

            state().resetLatestCall();
            state().logGameConsoleMessage(String.format(SHAKE_THE_CUP, state().getCurrentPlayer().getName()));

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
        if (!state().getCup().getCupActor().isMoving()) {
            // If the player wants to blindly believe another players call and pass it on.
            if (state().isBelievingBlind()) {
                // Start next turn. We expect the user to want to do a blind pas (while increasing his call).
                nextPlayer();

                if (state().getLatestCall().getNumberCombination().equals(NumberCombination.MAX)) {
                    believe666();
                    return true;
                } else {
                    blindBelieve();
                    return true;
                }
                // Blindly pass your own throw.
            } else if (state().isInitiatingBlindPass()) {
                state().getCup().lock();

                // Leave blindPass true on purpose.
                state().setCanViewOwnThrow(false);
                state().allowPlayerToCall(true);
                state().logGameConsoleMessage(NOW_ENTER_YOUR_CALL);
                return true;
                // Unlocking halfway the turn.
            } else if (state().bailedOutOfBlindBelieving()) {
                state().getCup().unlock();
                state().getCup().believe();

                // Don't set blindPass to false, the player simply has to throw again and can call without watching and then it will be set to blind again.
                state().setHasToThrow(true);
                state().setCanViewOwnThrow(true);
                state().allowPlayerToCall(false);
                state().logGameConsoleMessage(state().getCurrentPlayer().getName() + WANTED_TO_PEEK_AFTER_ALL);

                if (!leftDiceActor.isUnderCup()) {
                    leftDiceActor.lock();
                } else {
                    leftDiceActor.unlock();
                }

                if (!middleDiceActor.isUnderCup()) {
                    middleDiceActor.lock();
                } else {
                    middleDiceActor.unlock();
                }

                if (!rightDiceActor.isUnderCup()) {
                    rightDiceActor.lock();
                } else {
                    rightDiceActor.unlock();
                }
                return true;
                // Just locking / unlocking.
            } else if (state().userTriesToLockOrUnlock()) {
                // Very important to use hasToThrow and not isAllowedToThrow().
                if (state().getCup().isLocked()) {
                    state().getCup().unlock();
                    if (leftDiceActor.isUnderCup()) {
                        leftDiceActor.unlock();
                    }
                    if (middleDiceActor.isUnderCup()) {
                        middleDiceActor.unlock();
                    }
                    if (rightDiceActor.isUnderCup()) {
                        rightDiceActor.unlock();
                    }
                } else {
                    state().getCup().lock();
                    if (leftDiceActor.isUnderCup()) {
                        leftDiceActor.lock();
                    }
                    if (middleDiceActor.isUnderCup()) {
                        middleDiceActor.lock();
                    }
                    if (rightDiceActor.isUnderCup()) {
                        rightDiceActor.lock();
                    }
                }
                return true;
            } else {
                throw new IllegalStateException(String.format("Illegal appState detected: blindPass=%b, hasThrown=%b, hasToThrow=%b, firstThrowSinceDeath=%b, allowedToBelieveOrNotBelieve=%b",
                                state().isBlindPass(), state().isHasThrown(), state().isHasThrown(), state().isFirstThrowSinceDeath(), state().isAllowedToBelieveOrNotBelieve()));
            }
        }
        return false;
    }

    private void believe() {
        state().logGameConsoleMessage(state().getCurrentPlayer().getName() + BELIEVED_THE_CALL);
        state().logGameConsoleMessage("Throw at least one dice ...");

        if (getNumberCombinationFromDices().isGreaterThan(new NumberCombination(6,0,0, true))) {
            userInterface.showTutorialMessage(TutorialMessage.MOVE_SIX_OUT, state().getLatestCall().getPlayerName(), state().getLatestCall().getNumberCombination().toString());
        } else {
            userInterface.showTutorialMessage(TutorialMessage.RETHROW_ALL_DICES, state().getLatestCall().getPlayerName(), state().getLatestCall().getNumberCombination().toString());
        }

        postBelieveUiUpdate();
    }

    private void believe666() {
        // TODO: Do we need to make the dices visible here?
        state().setBelieved666(true);

        // TODO: Do we really need to put them back under the cup?
        leftDiceActor.putBackUnderCup();
        middleDiceActor.putBackUnderCup();
        rightDiceActor.putBackUnderCup();
        state().logGameConsoleMessage(state().getCurrentPlayer().getName() + BELIEVED_666);
        state().logGameConsoleMessage(THROW_THREE_OF_THE_SAME_NUMBERS_IN_ONE_THROW);

        postBelieveUiUpdate();
    }

    private void blindBelieve() {
        state().logGameConsoleMessage(state().getCurrentPlayer().getName() + BELIEVED_THE_CALL_BLIND);
        state().getCup().lock();

        // Lock the dices in case they are lying outside of the cup.
        leftDiceActor.lock();
        middleDiceActor.lock();
        rightDiceActor.lock();

        state().setAllowedToBelieveOrNotBelieve(false);
        state().setHasToThrow(false);
        state().setHasThrown(false);
        state().setBlindPass(true);
        state().allowPlayerToCall(true);
        state().logGameConsoleMessage(NOW_ENTER_YOUR_CALL_OR_THROW);
    }

    private void postBelieveUiUpdate() {
        state().setHasToThrow(true);
        state().setHasThrown(false);

        if (!leftDiceActor.isUnderCup() && leftDiceActor.getDiceValue() == 6) {
            leftDiceActor.lock();
        }
        if (!middleDiceActor.isUnderCup() && middleDiceActor.getDiceValue() == 6) {
            middleDiceActor.lock();
        }
        if (!rightDiceActor.isUnderCup() && rightDiceActor.getDiceValue() == 6) {
            rightDiceActor.lock();
        }

        state().getCup().believe();
        state().setBlindPass(false);

        lookAtOwnThrowMessageHasBeenShown = false;
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

        updatePlayerIterator();

        if (state().getPlayers()[state().getPlayerIterator()].isDead()) {
            nextPlayer();
        }

        // There is more than one player left
        return true;
    }

    public void updatePlayerIterator() {
        if (state().getPlayerIterator() + 1 < state().getPlayers().length) {
            state().updatePlayerIterator(state().getPlayerIterator() + 1);
        } else {
            state().updatePlayerIterator(0);
        }
    }

    private Player getWinner() {
        int numberOfPlayersStillAlive = 0;

        // Check if there are still more than two players alive.
        int indexOfLastLivingPlayer = 0;
        for (int i = 0; i < state().getPlayers().length; i++) {
            Player p = state().getPlayers()[i];
            if (!p.isDead()) {
                numberOfPlayersStillAlive++;
                indexOfLastLivingPlayer = i;
            }
        }

        if (numberOfPlayersStillAlive < 2) {
            // Set the winning player as the one to start next game.
            state().updatePlayerIterator(indexOfLastLivingPlayer);

            return state().getCurrentPlayer();
        } else {
            return null;
        }
    }

    private boolean canUseBok() {
        if (settingsState.isAllowBok()) {
            if (state().isBokAvailable()) {
                return true;
            } else {
                if (settingsState.isAllowSharedBok()) {
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
        state().getCup().getCupActor().reset();
        diceRoll.play(1.0f);

        final DiceActor.ThrowResult leftResult = leftDiceActor.throwDice();
        final DiceActor.ThrowResult middleResult = middleDiceActor.throwDice();
        final DiceActor.ThrowResult rightResult = rightDiceActor.throwDice();

        hasToThrow = false;
        hasThrown = true;
        canViewOwnThrow = true;

        state().allowPlayerToCall(true);

        if (leftResult == DiceActor.ThrowResult.UNDER_CUP || middleResult == DiceActor.ThrowResult.UNDER_CUP || rightResult == DiceActor.ThrowResult.UNDER_CUP) {
            state().setBlindPass(true); // Everytime you throw a dice under the cup, it starts out as a blind pass.
        }

        state().getCup().unlock();
        leftDiceActor.unlock();
        middleDiceActor.unlock();
        rightDiceActor.unlock();

        if (state().isFirstThrowSinceDeath()) {
            state().setCallInput(NumberCombination.MIN.toString());
            state().logGameConsoleMessage(String.format(WATCH_OWN_THROW, state().getCurrentPlayer().getName()));
            userInterface.showTutorialMessage(TutorialMessage.FIRST_THROWN_SINCE_DEATH);
        } else {
            state().logGameConsoleMessage(NOW_ENTER_YOUR_CALL);
        }
    }

    /**
     * @return A NumberCombination object based on the values of the dices.
     */
    public NumberCombination getNumberCombinationFromDices() {
        return new NumberCombination(leftDiceActor.getDiceValue(), middleDiceActor.getDiceValue(), rightDiceActor.getDiceValue(), true);
    }

    public GameState state() {
        return GameState.get();
    }
}
