package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Gdx;

import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.enums.TutorialMessage;
import net.leejjon.bluffpoker.interfaces.UserInterface;
import net.leejjon.bluffpoker.listener.CupListener;
import net.leejjon.bluffpoker.listener.DiceListener;
import net.leejjon.bluffpoker.logic.BluffPokerGame;
import net.leejjon.bluffpoker.logic.NumberCombination;

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

    private BluffPokerGame moveLeftDiceOut(BluffPokerGame game) {
        DiceListener diceListener = (DiceListener) state().getLeftDice().getDiceActor().getListeners().get(0);
        diceListener.tap(null, 0, 0 , 0, 0);
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

        GameStateEnum.NEW_GAME.assertUserInterfaceState(callInputLabel, autoButton, callButton, dicesUnderCupActors, NumberCombination.BLUFF_NUMBER);
        GameStateEnum.NEW_GAME.assertState(newGame, NumberCombination.BLUFF_NUMBER);

        BluffPokerGame reloadedGame = reloadGame();
        GameStateEnum.NEW_GAME.assertUserInterfaceState(callInputLabel, autoButton, callButton, dicesUnderCupActors, NumberCombination.BLUFF_NUMBER);
        GameStateEnum.NEW_GAME.assertState(reloadedGame, NumberCombination.BLUFF_NUMBER);
    }

    private NumberCombination get641() {
        return new NumberCombination(6, 4, 1, false);
    }

    @Test
    public void testFirstThrow_641() {
        NumberCombination expectedNumberCombination = get641();

        // Add 530 will result in displaying 641. The random generator always generates 0-5 instead of 1-6, so the method that uses the generator will add +1.
        BluffPokerGame game = throwSpecificValue(startNewGame(), expectedNumberCombination);
        GameStateEnum.AFTER_FIRST_SHAKE.assertUserInterfaceState(callInputLabel, autoButton, callButton, dicesUnderCupActors, expectedNumberCombination);
        GameStateEnum.AFTER_FIRST_SHAKE.assertState(game, expectedNumberCombination);

        BluffPokerGame reloadedGame = reloadGame();
        GameStateEnum.AFTER_FIRST_SHAKE.assertUserInterfaceState(callInputLabel, autoButton, callButton, dicesUnderCupActors, expectedNumberCombination);
        GameStateEnum.AFTER_FIRST_SHAKE.assertState(reloadedGame, expectedNumberCombination);
    }

    @Test
    public void testFirstThrow_641_thenReset() {
        NumberCombination expectedNumberCombination = get641();
        BluffPokerGame game = throwSpecificValue(startNewGame(), expectedNumberCombination);

        reloadGame();
        GameState.reset();

        // Basically the same happens as in the ContinueGameDialog when you click no.
        BluffPokerGame gameThatHasBeenReset = initializeGame();
        game.startGame(loadDefaultPlayers());

        GameStateEnum.NEW_GAME.assertUserInterfaceState(callInputLabel, autoButton, callButton, dicesUnderCupActors, NumberCombination.BLUFF_NUMBER);
        GameStateEnum.NEW_GAME.assertState(gameThatHasBeenReset, NumberCombination.BLUFF_NUMBER);
    }

    @Test
    public void testFirstThrow_641_thenViewOwnThrow() {
        NumberCombination expectedNumberCombination = get641();
        BluffPokerGame game = tapCup(throwSpecificValue(startNewGame(), expectedNumberCombination));

        GameStateEnum.VIEW_AFTER_FIRST_SHAKE.assertUserInterfaceState(callInputLabel, autoButton, callButton, dicesUnderCupActors, expectedNumberCombination);
        GameStateEnum.VIEW_AFTER_FIRST_SHAKE.assertState(game, expectedNumberCombination);
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
        playerList.add("Leon");
        playerList.add("Dirk");
        return playerList;
    }
}
