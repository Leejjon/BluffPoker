package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Gdx;

import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.enums.TutorialMessage;
import net.leejjon.bluffpoker.interfaces.UserInterface;
import net.leejjon.bluffpoker.logic.BluffPokerGame;
import net.leejjon.bluffpoker.logic.NumberCombination;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.fail;
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
        GameState gameState = GameState.get();

        try {
            gameState.logGameConsoleMessage("Blabla");
            fail("Should not be able to log if UI is not initialized.");
        } catch (NullPointerException e) {
        }

        initializeUI(gameState);

        assertTrue(gameState.isNewGameState());

        String a = "a";
        String b = "b";
        String c = "c";

        gameState.logGameConsoleMessage(a);

        assertFalse(gameState.isNewGameState());
        verify(preferences, times(1)).flush();

        assertEquals(a, latestOutputLabel.getText().toString());
        assertEquals(a, gameState.latestOutput);

        gameState.logGameConsoleMessage(b);
        gameState.logGameConsoleMessage(c);

        assertEquals(a, thirdLatestOutputLabel.getText().toString());
        assertEquals(b, secondLatestOutputLabel.getText().toString());
        assertEquals(c, latestOutputLabel.getText().toString());
        assertEquals(a, gameState.thirdLatestOutput);
        assertEquals(b, gameState.secondLatestOutput);
        assertEquals(c, gameState.latestOutput);

        verify(preferences, times(3)).flush();
    }

    private BluffPokerGame startNewGame() {
        GameState gameState = GameState.get();
        initializeUI(gameState);

        BluffPokerGame game = initializeGame();
        game.startGame(loadDefaultPlayers());
        return game;
    }

    private BluffPokerGame initializeGame() {
        return new BluffPokerGame(diceRoll, getTestUserInterface());
    }

    private BluffPokerGame throwSpecificValue(BluffPokerGame game, int left, int middle, int right) {
        predictableDiceValueGenerator.add(left, middle, right);

        game.throwDices();
        return game;
    }

    private BluffPokerGame reloadGame() {
        String stateString = GameState.getStateString();
        // From this point, reset and reload the game.
        GameState.get().resetToNull();
        when(preferences.getString(GameState.KEY)).thenReturn(stateString);
        initializeUI(GameState.get());

        return initializeGame();
    }

    @Test
    public void testNewGame() {
        BluffPokerGame newGame = startNewGame();

        GameStateEnum.NEW_GAME.assertGameState(GameState.get(), NumberCombination.BLUFF_NUMBER);
        GameStateEnum.NEW_GAME.assertUserInterfaceState(GameState.get(), callInputLabel, autoButton, callButton, dicesUnderCupActors);
        GameStateEnum.NEW_GAME.assertBluffPokerGame(newGame, NumberCombination.BLUFF_NUMBER);

        BluffPokerGame reloadedGame = reloadGame();
        GameStateEnum.NEW_GAME.assertGameState(GameState.get(), NumberCombination.BLUFF_NUMBER);
        GameStateEnum.NEW_GAME.assertUserInterfaceState(GameState.get(), callInputLabel, autoButton, callButton, dicesUnderCupActors);
        GameStateEnum.NEW_GAME.assertBluffPokerGame(reloadedGame, NumberCombination.BLUFF_NUMBER);
    }

    @Test
    public void testFirstThrow_641() {
        NumberCombination expectedNumberCombination = new NumberCombination(6, 4, 1, false);

        // Add 530 will result in displaying 641. The random generator always generates 0-5 instead of 1-6, so the method that uses the generator will add +1.
        BluffPokerGame game = throwSpecificValue(startNewGame(),5, 3, 0);
        GameStateEnum.AFTER_FIRST_SHAKE.assertGameState(GameState.get(), expectedNumberCombination); // TODO: Make sure all enum methods are implemented correctly.
        GameStateEnum.AFTER_FIRST_SHAKE.assertBluffPokerGame(game, expectedNumberCombination);

        BluffPokerGame reloadedGame = reloadGame();
        GameStateEnum.AFTER_FIRST_SHAKE.assertGameState(GameState.get(), expectedNumberCombination);
        GameStateEnum.AFTER_FIRST_SHAKE.assertBluffPokerGame(reloadedGame, expectedNumberCombination);
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
