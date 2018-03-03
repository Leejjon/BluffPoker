package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Gdx;

import net.leejjon.bluffpoker.BluffPokerApp;
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

    @Test
    public void testLogging() {
        String logMesssage = "Hoi";
        Gdx.app.log(BluffPokerApp.TAG, logMesssage);

        assertEquals(logMesssage, logMessages.get(0));
    }

    @Test
    public void testConsoleLogging_threeMessages_success() {
        try {
            state().logGameConsoleMessage("Blabla");
            fail("Should not be able to log if UI is not initialized.");
        } catch (NullPointerException e) {
        }

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

    private BluffPokerGame call(BluffPokerGame game, String call) throws InputValidationException {
        game.validateCall(NumberCombination.validNumberCombinationFrom(call));
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
        GameStateEnum.NEW_GAME.assertState(newGame, NumberCombination.BLUFF_NUMBER, null, getPlayer1());

        GameStateEnum.NEW_GAME.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, NumberCombination.BLUFF_NUMBER);
        GameStateEnum.NEW_GAME.assertState(reloadGame(), NumberCombination.BLUFF_NUMBER, null, getPlayer1());
    }

    private NumberCombination get641() {
        return new NumberCombination(6, 4, 1, false);
    }

    @Test
    public void testFirstThrow641() {
        NumberCombination expectedNumberCombination = get641();

        // Add 530 will result in displaying 641. The random generator always generates 0-5 instead of 1-6, so the method that uses the generator will add +1.
        BluffPokerGame game = throwSpecificValue(startNewGame(), expectedNumberCombination);
        GameStateEnum.AFTER_FIRST_SHAKE.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.AFTER_FIRST_SHAKE.assertState(game, expectedNumberCombination, null, getPlayer1());

        GameStateEnum.AFTER_FIRST_SHAKE.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.AFTER_FIRST_SHAKE.assertState(reloadGame(), expectedNumberCombination, null, getPlayer1());
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
        GameStateEnum.NEW_GAME.assertState(gameThatHasBeenReset, NumberCombination.BLUFF_NUMBER, null, getPlayer1());
    }

    @Test
    public void testFirstThrow641_blindCall641() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        BluffPokerGame game = call(throwSpecificValue(startNewGame(), expectedNumberCombination), expectedNumberCombination.toString());

        GameStateEnum.CALL_BLIND.assertUserInterfaceState(expectedNumberCombination.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.CALL_BLIND.assertState(game, expectedNumberCombination, expectedNumberCombination, getPlayer1());

        GameStateEnum.CALL_BLIND.assertUserInterfaceState(expectedNumberCombination.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.CALL_BLIND.assertState(reloadGame(), expectedNumberCombination, expectedNumberCombination, getPlayer1());

        assertTrue(logMessages.get(logMessages.size - 1).contains(BluffPokerGame.BLIND_MESSAGE));
    }

    @Test
    public void testFirstThrow641_view() {
        NumberCombination expectedNumberCombination = get641();
        BluffPokerGame game = tapCup(throwSpecificValue(startNewGame(), expectedNumberCombination));

        GameStateEnum.VIEW_AFTER_FIRST_SHAKE.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.VIEW_AFTER_FIRST_SHAKE.assertState(game, expectedNumberCombination, null, getPlayer1());

        GameStateEnum.VIEW_AFTER_FIRST_SHAKE.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.VIEW_AFTER_FIRST_SHAKE.assertState(reloadGame(), expectedNumberCombination, null, getPlayer1());
    }

    @Test
    public void testFirstThrow641_view_call600() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        BluffPokerGame game = call(tapCup(throwSpecificValue(startNewGame(), expectedNumberCombination)), expectedNumberCombination.toString());

        GameStateEnum.CALL.assertUserInterfaceState(expectedNumberCombination.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.CALL.assertState(game, expectedNumberCombination, expectedNumberCombination, getPlayer1());

        GameStateEnum.CALL.assertUserInterfaceState(expectedNumberCombination.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.CALL.assertState(reloadGame(), expectedNumberCombination, expectedNumberCombination, getPlayer1());
    }

    @Test
    public void testFirstThrow641_view_moveOut6() {
        NumberCombination expectedNumberCombination = get641();
        BluffPokerGame game = moveLeftDiceOut(tapCup(throwSpecificValue(startNewGame(), expectedNumberCombination)));

        GameStateEnum.TAKE_6_OUT.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.TAKE_6_OUT.assertState(game, expectedNumberCombination, null, getPlayer1());

        GameStateEnum.TAKE_6_OUT.assertUserInterfaceState(NumberCombination.MIN.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.TAKE_6_OUT.assertState(reloadGame(), expectedNumberCombination, null, getPlayer1());
    }

    @Test
    public void testFirstThrow641_view_moveOut6_call600() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        BluffPokerGame game = call(moveLeftDiceOut(tapCup(throwSpecificValue(startNewGame(), expectedNumberCombination))), expectedNumberCombination.toString());

        GameStateEnum.CALL_LEFT_SIX_OUT.assertUserInterfaceState(expectedNumberCombination.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.CALL_LEFT_SIX_OUT.assertState(game, expectedNumberCombination, expectedNumberCombination, getPlayer1());

        GameStateEnum.CALL_LEFT_SIX_OUT.assertUserInterfaceState(expectedNumberCombination.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.CALL_LEFT_SIX_OUT.assertState(reloadGame(), expectedNumberCombination, expectedNumberCombination, getPlayer1());
    }

    @Test
    public void testFirstThrow641_blindCall641_believe() throws InputValidationException {
        NumberCombination expectedNumberCombination = get641();
        BluffPokerGame game = tapCup(call(throwSpecificValue(startNewGame(), expectedNumberCombination), expectedNumberCombination.toString()));

        GameStateEnum.BELIEVED_ALL_DICES_UNDER_CUP.assertUserInterfaceState(expectedNumberCombination.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.BELIEVED_ALL_DICES_UNDER_CUP.assertState(game, expectedNumberCombination, expectedNumberCombination, getPlayer2());

        GameStateEnum.BELIEVED_ALL_DICES_UNDER_CUP.assertUserInterfaceState(expectedNumberCombination.toString(),
                callInputLabel, autoButton, callButton, dicesUnderCupActors, dicesBeforeCupActors, expectedNumberCombination);
        GameStateEnum.BELIEVED_ALL_DICES_UNDER_CUP.assertState(reloadGame(), expectedNumberCombination, expectedNumberCombination, getPlayer2());
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

    private Player getPlayer1() {
        return new Player(PLAYER_1, 3);
    }

    private Player getPlayer2() {
        return new Player(PLAYER_2, 3);
    }
}
