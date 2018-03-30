package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Gdx;

import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.actors.CupActor;
import net.leejjon.bluffpoker.enums.TutorialMessage;
import net.leejjon.bluffpoker.interfaces.UserInterface;
import net.leejjon.bluffpoker.listener.CupListener;
import net.leejjon.bluffpoker.listener.DiceListener;
import net.leejjon.bluffpoker.logic.BluffPokerGame;
import net.leejjon.bluffpoker.logic.InputValidationException;
import net.leejjon.bluffpoker.logic.NumberCombination;
import net.leejjon.bluffpoker.logic.Player;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.fail;
import static net.leejjon.bluffpoker.state.GameState.state;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameStateTest extends GdxTest {
    private static final String PLAYER_1 = "Leon";
    private static final String PLAYER_2 = "Dirk";
    private static final int DEFAULT_LIVES = 3;
    private static final int LOST_ONE_LIFE = 2;

    @Test
    public void testLogging() {
        String logMesssage = "Hoi";
        Gdx.app.log(BluffPokerApp.TAG, logMesssage);

        assertEquals(logMesssage, logMessages.get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testLogConsoleMessageWithoutUI_fail() {
        state().logGameConsoleMessage("Blabla");
        fail("Should not be able to log if UI is not initialized.");
    }

    @Test
    public void testConsoleLogging_threeMessages_success() {
        initializeUI();

        assertTrue(state().isNewGameState());

        String a = "a";
        String b = "b";
        String c = "c";

        state().logGameConsoleMessage(a);

        assertFalse(state().isNewGameState());
        verify(preferences, times(1)).flush();

        assertEquals(a, latestOutputLabel.getText().toString());
        assertEquals(a, state().latestOutput);

        state().logGameConsoleMessage(b);
        state().logGameConsoleMessage(c);

        assertEquals(a, thirdLatestOutputLabel.getText().toString());
        assertEquals(b, secondLatestOutputLabel.getText().toString());
        assertEquals(c, latestOutputLabel.getText().toString());
        assertEquals(a, state().thirdLatestOutput);
        assertEquals(b, state().secondLatestOutput);
        assertEquals(c, state().latestOutput);

        verify(preferences, times(3)).flush();
    }

    private BluffPokerGame startNewGame() {
        initializeUI();

        BluffPokerGame game = initializeGame();
        game.startGame(loadDefaultPlayers());
        return game;
    }

    private BluffPokerGame initializeGame() {
        return new BluffPokerGame(diceRoll, getTestUserInterface());
    }

    private BluffPokerGame throwSpecificValue(BluffPokerGame game, NumberCombination numberCombination) {
        predictableDiceValueGenerator.add(numberCombination.getHighestNumber() - 1, numberCombination.getMiddleNumber() - 1, numberCombination.getLowestNumber() - 1);

        game.throwDices();
        return game;
    }

    private BluffPokerGame tapCup(BluffPokerGame game) {
        CupListener cupListener = (CupListener) state().getCup().getCupActor().getListeners().get(0);
        cupListener.tap(null, 0, 0, 0, 0);
        return game;
    }

    private BluffPokerGame swipeCupUpAndFinishAnimation(BluffPokerGame game) {
        CupActor cupActor = state().getCup().getCupActor();
        CupListener cupListener = (CupListener) cupActor.getListeners().get(0);
        cupListener.fling(null , -1, 1, 0);
        cupActor.getActions().clear();
        return game;
    }

    private BluffPokerGame longTapCup(BluffPokerGame game) {
        CupListener cupListener = (CupListener) state().getCup().getCupActor().getListeners().get(0);
        cupListener.longPress(null, 0f, 0f);
        return game;
    }

    private BluffPokerGame moveLeftDiceOut(BluffPokerGame game) {
        DiceListener diceListener = (DiceListener) state().getLeftDice().getDiceActor().getListeners().get(0);
        diceListener.tap(null, 0, 0 , 0, 0);
        return game;
    }

    private BluffPokerGame swipeLeftDiceUp(BluffPokerGame game) {
        DiceListener diceListener = (DiceListener) state().getLeftDice().getDiceActor().getListeners().get(0);
        diceListener.fling(null, 0, 1, 0);
        return game;
    }

    private BluffPokerGame call(BluffPokerGame game, NumberCombination call) throws InputValidationException {
        game.validateCall(call);
        return game;
    }

    private BluffPokerGame reloadGame() {
        String stateString = GameState.getStateString();

        reset();
        setup();

        when(preferences.getString(GameState.KEY)).thenReturn(stateString);

        initializeUI();
        return initializeGame();
    }

    @Test
    public void testNewGameFromFirstInstallation() {
        BluffPokerGame newGame = startNewGame();

        GameStateEnum.NEW_GAME.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, NumberCombination.BLUFF_NUMBER);
        GameStateEnum.NEW_GAME.assertState(newGame, NumberCombination.BLUFF_NUMBER, null, NumberCombination.MIN, getPlayer1(DEFAULT_LIVES));

        GameStateEnum.NEW_GAME.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, NumberCombination.BLUFF_NUMBER);
        GameStateEnum.NEW_GAME.assertState(reloadGame(), NumberCombination.BLUFF_NUMBER, null, NumberCombination.MIN, getPlayer1(DEFAULT_LIVES));
    }

    private NumberCombination get641() {
        return new NumberCombination(6, 4, 1, false);
    }

    private NumberCombination call600() {
        return new NumberCombination(6,0,0, true);
    }

    @Test
    public void testFirstThrow641() {
        NumberCombination expectedNumberCombination = get641();

        // Add 530 will result in displaying 641. The random generator always generates 0-5 instead of 1-6, so the method that uses the generator will add +1.
        BluffPokerGame game = throwSpecificValue(startNewGame(), expectedNumberCombination);
        GameStateEnum.AFTER_FIRST_SHAKE.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.AFTER_FIRST_SHAKE.assertState(game, expectedNumberCombination, null, NumberCombination.MIN, getPlayer1(DEFAULT_LIVES));

        GameStateEnum.AFTER_FIRST_SHAKE.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.AFTER_FIRST_SHAKE.assertState(reloadGame(), expectedNumberCombination, null, NumberCombination.MIN, getPlayer1(DEFAULT_LIVES));
    }

    @Test
    public void testFirstThrow641_thenReset() {
        NumberCombination expectedNumberCombination = get641();
        BluffPokerGame game = throwSpecificValue(startNewGame(), expectedNumberCombination);

        reloadGame();
        GameState.reset();

        // Basically the same happens as in the ContinueGameDialog when you click no.
        BluffPokerGame gameThatHasBeenReset = initializeGame();
        game.startGame(loadDefaultPlayers());

        GameStateEnum.NEW_GAME.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, NumberCombination.BLUFF_NUMBER);
        GameStateEnum.NEW_GAME.assertState(gameThatHasBeenReset, NumberCombination.BLUFF_NUMBER, null, NumberCombination.MIN, getPlayer1(DEFAULT_LIVES));
    }

    @Test
    public void testFirstThrow641_blindCall600() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        NumberCombination call = call600();
        BluffPokerGame game = call(throwSpecificValue(startNewGame(), expectedNumberCombination), call);

        GameStateEnum.CALL_BLIND.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.CALL_BLIND.assertState(game, expectedNumberCombination, call, call, getPlayer1(DEFAULT_LIVES));

        GameStateEnum.CALL_BLIND.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.CALL_BLIND.assertState(reloadGame(), expectedNumberCombination, call, call, getPlayer1(DEFAULT_LIVES));

        assertTrue(logMessages.get(logMessages.size - 1).contains(BluffPokerGame.BLIND_MESSAGE));
    }

    @Test
    public void testFirstThrow641_blindCall643() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        BluffPokerGame game = call(throwSpecificValue(startNewGame(), expectedNumberCombination), NumberCombination.BLUFF_NUMBER);

        GameStateEnum.CALL_BLIND.assertUserInterfaceState(NumberCombination.BLUFF_NUMBER.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.CALL_BLIND.assertState(game, expectedNumberCombination, NumberCombination.BLUFF_NUMBER, NumberCombination.BLUFF_NUMBER, getPlayer1(DEFAULT_LIVES));

        GameStateEnum.CALL_BLIND.assertUserInterfaceState(NumberCombination.BLUFF_NUMBER.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.CALL_BLIND.assertState(reloadGame(), expectedNumberCombination, NumberCombination.BLUFF_NUMBER, NumberCombination.BLUFF_NUMBER, getPlayer1(DEFAULT_LIVES));

        assertTrue(logMessages.get(logMessages.size - 1).contains(BluffPokerGame.BLIND_MESSAGE));
    }

    @Test
    public void testFirstThrow641_view() {
        NumberCombination expectedNumberCombination = get641();
        BluffPokerGame game = tapCup(throwSpecificValue(startNewGame(), expectedNumberCombination));

        GameStateEnum.VIEW_AFTER_FIRST_SHAKE.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.VIEW_AFTER_FIRST_SHAKE.assertState(game, expectedNumberCombination, null, NumberCombination.MIN, getPlayer1(DEFAULT_LIVES));

        GameStateEnum.VIEW_AFTER_FIRST_SHAKE.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.VIEW_AFTER_FIRST_SHAKE.assertState(reloadGame(), expectedNumberCombination, null, NumberCombination.MIN, getPlayer1(DEFAULT_LIVES));
    }

    @Test
    public void testFirstThrow641_view_swipeDiceDownAndUp() {
        NumberCombination expectedNumberCombination = get641();
        BluffPokerGame game = swipeLeftDiceUp(moveLeftDiceOut(tapCup(throwSpecificValue(startNewGame(), expectedNumberCombination))));

        GameStateEnum.VIEW_AFTER_FIRST_SHAKE.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.VIEW_AFTER_FIRST_SHAKE.assertState(game, expectedNumberCombination, null, NumberCombination.MIN, getPlayer1(DEFAULT_LIVES));

        GameStateEnum.VIEW_AFTER_FIRST_SHAKE.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.VIEW_AFTER_FIRST_SHAKE.assertState(reloadGame(), expectedNumberCombination, null, NumberCombination.MIN, getPlayer1(DEFAULT_LIVES));
    }

    @Test
    public void testFirstThrow641_view_call600() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        NumberCombination call = call600();
        BluffPokerGame game = call(tapCup(throwSpecificValue(startNewGame(), expectedNumberCombination)), call);

        GameStateEnum.CALL.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.CALL.assertState(game, expectedNumberCombination, call, call, getPlayer1(DEFAULT_LIVES));

        GameStateEnum.CALL.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.CALL.assertState(reloadGame(), expectedNumberCombination, call, call, getPlayer1(DEFAULT_LIVES));
    }

    @Test
    public void testFirstThrow641_view_moveOut6() {
        NumberCombination expectedNumberCombination = get641();
        BluffPokerGame game = moveLeftDiceOut(tapCup(throwSpecificValue(startNewGame(), expectedNumberCombination)));

        GameStateEnum.TAKE_6_OUT.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.TAKE_6_OUT.assertState(game, expectedNumberCombination, null, NumberCombination.MIN, getPlayer1(DEFAULT_LIVES));

        GameStateEnum.TAKE_6_OUT.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.TAKE_6_OUT.assertState(reloadGame(), expectedNumberCombination, null, NumberCombination.MIN, getPlayer1(DEFAULT_LIVES));
    }

    @Test
    public void testFirstThrow641_view_moveOut6_call600() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        NumberCombination call = call600();
        BluffPokerGame game = call(moveLeftDiceOut(tapCup(throwSpecificValue(startNewGame(), expectedNumberCombination))), call);

        GameStateEnum.CALL_LEFT_SIX_OUT.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.CALL_LEFT_SIX_OUT.assertState(game, expectedNumberCombination, call, call, getPlayer1(DEFAULT_LIVES));

        GameStateEnum.CALL_LEFT_SIX_OUT.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.CALL_LEFT_SIX_OUT.assertState(reloadGame(), expectedNumberCombination, call, call, getPlayer1(DEFAULT_LIVES));
    }

    @Test
    public void testFirstThrow641_blindCall600_believe() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        NumberCombination call = call600();
        BluffPokerGame game = tapCup(call(throwSpecificValue(startNewGame(), expectedNumberCombination), call));

        GameStateEnum.BELIEVED_ALL_DICES_UNDER_CUP.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.BELIEVED_ALL_DICES_UNDER_CUP.assertState(game, expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));

        GameStateEnum.BELIEVED_ALL_DICES_UNDER_CUP.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.BELIEVED_ALL_DICES_UNDER_CUP.assertState(reloadGame(), expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));
    }

    @Test
    public void testFirstThrow641_blindCall600_blindBelieve() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        NumberCombination call = call600();
        BluffPokerGame game = longTapCup(call(throwSpecificValue(startNewGame(), expectedNumberCombination), call));

        GameStateEnum.BELIEVED_ALL_DICES_UNDER_CUP_BLIND.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.BELIEVED_ALL_DICES_UNDER_CUP_BLIND.assertState(game, expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));

        GameStateEnum.BELIEVED_ALL_DICES_UNDER_CUP_BLIND.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.BELIEVED_ALL_DICES_UNDER_CUP_BLIND.assertState(reloadGame(), expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));
    }

    @Test
    public void testFirstThrow641_blindCall666_believe() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        NumberCombination call = NumberCombination.MAX;
        BluffPokerGame game = tapCup(call(throwSpecificValue(startNewGame(), expectedNumberCombination), call));

        GameStateEnum.BELIEVED_666_ALL_DICES_UNDER_CUP.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.BELIEVED_666_ALL_DICES_UNDER_CUP.assertState(game, expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));

        GameStateEnum.BELIEVED_666_ALL_DICES_UNDER_CUP.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.BELIEVED_666_ALL_DICES_UNDER_CUP.assertState(reloadGame(), expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));
    }

    @Test
    public void testFirstThrow641_blindCall666_believe_closed() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        NumberCombination call = NumberCombination.MAX;
        BluffPokerGame game = tapCup(tapCup(call(throwSpecificValue(startNewGame(), expectedNumberCombination), call)));

        GameStateEnum.BELIEVED_666_ALL_DICES_UNDER_CUP_CLOSED.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.BELIEVED_666_ALL_DICES_UNDER_CUP_CLOSED.assertState(game, expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));

        GameStateEnum.BELIEVED_666_ALL_DICES_UNDER_CUP_CLOSED.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.BELIEVED_666_ALL_DICES_UNDER_CUP_CLOSED.assertState(reloadGame(), expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));
    }

    @Test
    public void testFirstThrow641_blindCall666_believe_closed_thrown() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        NumberCombination call = NumberCombination.MAX;
        // After believing 666 we just throw 641 again.
        BluffPokerGame game = throwSpecificValue(tapCup(tapCup(call(throwSpecificValue(startNewGame(), expectedNumberCombination), call))), expectedNumberCombination);

        GameStateEnum.BELIEVED_666_ALL_DICES_UNDER_CUP_CLOSED_THROWN.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.BELIEVED_666_ALL_DICES_UNDER_CUP_CLOSED_THROWN.assertState(game, expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));

        GameStateEnum.BELIEVED_666_ALL_DICES_UNDER_CUP_CLOSED_THROWN.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.BELIEVED_666_ALL_DICES_UNDER_CUP_CLOSED_THROWN.assertState(reloadGame(), expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));
    }

    @Test
    public void testFirstThrow641_view_moveOut6_call600_believe() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        NumberCombination call = call600();

        BluffPokerGame game = tapCup(call(moveLeftDiceOut(tapCup(throwSpecificValue(startNewGame(), expectedNumberCombination))), call));

        GameStateEnum.BELIEVED_LEFT_SIX_OUT.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.BELIEVED_LEFT_SIX_OUT.assertState(game, expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));

        GameStateEnum.BELIEVED_LEFT_SIX_OUT.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.BELIEVED_LEFT_SIX_OUT.assertState(reloadGame(), expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));
    }

    @Test
    public void testFirstThrow641_blindCall643_dontBelieve_player1LosesLife() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        NumberCombination bluffNumber = NumberCombination.BLUFF_NUMBER;

        BluffPokerGame game = swipeCupUpAndFinishAnimation(call(throwSpecificValue(startNewGame(), expectedNumberCombination), bluffNumber));

        GameStateEnum.NOT_BELIEVE_CORRECT_All_DICES_UNDER_CUP.assertUserInterfaceState(bluffNumber.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        // We expect the latestCall object to be reset (null), but the callboard still to show the old value so the players can see the bluff.
        GameStateEnum.NOT_BELIEVE_CORRECT_All_DICES_UNDER_CUP.assertState(game, expectedNumberCombination, null, bluffNumber, getPlayer1(LOST_ONE_LIFE));

        GameStateEnum.NOT_BELIEVE_CORRECT_All_DICES_UNDER_CUP.assertUserInterfaceState(bluffNumber.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.NOT_BELIEVE_CORRECT_All_DICES_UNDER_CUP.assertState(reloadGame(), expectedNumberCombination, null, bluffNumber, getPlayer1(LOST_ONE_LIFE));
    }

    @Test
    public void testFirstThrow641_moveOut6_call643_dontBelieve_player1LosesLife() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        NumberCombination bluffNumber = NumberCombination.BLUFF_NUMBER;

        BluffPokerGame game = swipeCupUpAndFinishAnimation(call(moveLeftDiceOut(tapCup(throwSpecificValue(startNewGame(), expectedNumberCombination))), bluffNumber));

        GameStateEnum.NOT_BELIEVE_CORRECT_LEFT_DICE_OUT.assertUserInterfaceState(bluffNumber.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        // We expect the latestCall object to be reset (null), but the callboard still to show the old value so the players can see the bluff.
        GameStateEnum.NOT_BELIEVE_CORRECT_LEFT_DICE_OUT.assertState(game, expectedNumberCombination, null, bluffNumber, getPlayer1(LOST_ONE_LIFE));

        GameStateEnum.NOT_BELIEVE_CORRECT_LEFT_DICE_OUT.assertUserInterfaceState(bluffNumber.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.NOT_BELIEVE_CORRECT_LEFT_DICE_OUT.assertState(reloadGame(), expectedNumberCombination, null, bluffNumber, getPlayer1(LOST_ONE_LIFE));
    }

    @Test
    public void testFirstThrow641_blindCall600_dontBelieve_player2LosesLife() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        NumberCombination call = call600();
        BluffPokerGame game = swipeCupUpAndFinishAnimation(call(throwSpecificValue(startNewGame(), expectedNumberCombination), call));

        GameStateEnum.NOT_BELIEVE_INCORRECT_ALL_DICES_UNDER_CUP.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        // We expect the latestCall object to be reset (null), but the callboard still to show the old value so the players can see the bluff.
        GameStateEnum.NOT_BELIEVE_INCORRECT_ALL_DICES_UNDER_CUP.assertState(game, expectedNumberCombination, null, call, getPlayer2(LOST_ONE_LIFE));

        GameStateEnum.NOT_BELIEVE_INCORRECT_ALL_DICES_UNDER_CUP.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.NOT_BELIEVE_INCORRECT_ALL_DICES_UNDER_CUP.assertState(reloadGame(), expectedNumberCombination, null, call, getPlayer2(LOST_ONE_LIFE));
    }

    @Test
    public void testFirstThrow641_moveOut6_call600_dontBelieve_player2LosesLife() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        NumberCombination bluffNumber = call600();

        BluffPokerGame game = swipeCupUpAndFinishAnimation(call(moveLeftDiceOut(tapCup(throwSpecificValue(startNewGame(), expectedNumberCombination))), bluffNumber));

        GameStateEnum.NOT_BELIEVE_INCORRECT_LEFT_DICE_OUT.assertUserInterfaceState(bluffNumber.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        // We expect the latestCall object to be reset (null), but the callboard still to show the old value so the players can see the bluff.
        GameStateEnum.NOT_BELIEVE_INCORRECT_LEFT_DICE_OUT.assertState(game, expectedNumberCombination, null, bluffNumber, getPlayer2(LOST_ONE_LIFE));

        GameStateEnum.NOT_BELIEVE_INCORRECT_LEFT_DICE_OUT.assertUserInterfaceState(bluffNumber.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.NOT_BELIEVE_INCORRECT_LEFT_DICE_OUT.assertState(reloadGame(), expectedNumberCombination, null, bluffNumber, getPlayer2(LOST_ONE_LIFE));
    }

    @Test
    public void testBelieveCall600_closeCup() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        NumberCombination call = call600();
        BluffPokerGame game = tapCup(tapCup(call(throwSpecificValue(startNewGame(), expectedNumberCombination), call)));

        GameStateEnum.AFTER_BELIEVE_ALL_DICES_UNDER_CUP_CLOSE_CUP.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.AFTER_BELIEVE_ALL_DICES_UNDER_CUP_CLOSE_CUP.assertState(game, expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));

        GameStateEnum.AFTER_BELIEVE_ALL_DICES_UNDER_CUP_CLOSE_CUP.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.AFTER_BELIEVE_ALL_DICES_UNDER_CUP_CLOSE_CUP.assertState(reloadGame(), expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));
    }

    @Test
    public void test_blindBelieveCall600_peek() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        NumberCombination call = call600();
        BluffPokerGame game = longTapCup(
                              longTapCup(
                              call(
                              throwSpecificValue(
                              startNewGame(), expectedNumberCombination), call)));

        GameStateEnum.AFTER_BELIEVE_ALL_DICES_UNDER_CUP_BLIND_PEEK.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.AFTER_BELIEVE_ALL_DICES_UNDER_CUP_BLIND_PEEK.assertState(game, expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));

        GameStateEnum.AFTER_BELIEVE_ALL_DICES_UNDER_CUP_BLIND_PEEK.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.AFTER_BELIEVE_ALL_DICES_UNDER_CUP_BLIND_PEEK.assertState(reloadGame(), expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));
    }

    @Test
    public void test_blindBelieveCall600_peek_close() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        NumberCombination call = call600();
        BluffPokerGame game = tapCup(longTapCup(longTapCup(call(throwSpecificValue(startNewGame(), expectedNumberCombination), call))));

        GameStateEnum.AFTER_BELIEVE_ALL_DICES_UNDER_CUP_BLIND_PEEK_CLOSE.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.AFTER_BELIEVE_ALL_DICES_UNDER_CUP_BLIND_PEEK_CLOSE.assertState(game, expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));

        GameStateEnum.AFTER_BELIEVE_ALL_DICES_UNDER_CUP_BLIND_PEEK_CLOSE.assertUserInterfaceState(call.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.AFTER_BELIEVE_ALL_DICES_UNDER_CUP_BLIND_PEEK_CLOSE.assertState(reloadGame(), expectedNumberCombination, call, call, getPlayer2(DEFAULT_LIVES));
    }

    private UserInterface getTestUserInterface() {
        return new UserInterface() {
            @Override
            public void call(String call) {
            }

            @Override
            public void finishGame(String winner) {
            }

            @Override
            public void showTutorialMessage(TutorialMessage message, String... arguments) {
            }

            @Override
            public void showLockMessage() {
            }
        };
    }

    private ArrayList<String> loadDefaultPlayers() {
        ArrayList<String> playerList = new ArrayList<>();
        playerList.add(PLAYER_1);
        playerList.add(PLAYER_2);
        return playerList;
    }

    private Player getPlayer1(int lives) {
        return new Player(PLAYER_1, lives);
    }

    private Player getPlayer2(int lives) {
        return new Player(PLAYER_2, lives);
    }
}
