package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Gdx;

import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.enums.TutorialMessage;
import net.leejjon.bluffpoker.interfaces.UserInterface;
import net.leejjon.bluffpoker.logic.Game;

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
        Gdx.app.log(BluffPokerGame.TAG, logMesssage);

        assertEquals(logMesssage, logMessages.get(0));
    }

    @Test
    public void testConsoleLogging_threeMessages_success() {
        GameState gameState = GameState.get();

        try {
            gameState.logGameConsoleMessage("Blabla");
            fail("Should not be able to log if UI is not initialized.");
        } catch (NullPointerException e) {}

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

    @Test
    public void testNewGame_success() {
        GameState gameState = GameState.get();
        initializeUI(gameState);

        Game game = new Game(diceRoll, getTestUserInterface());

        game.startGame(loadDefaultPlayers());

        GameStateEnum.NEW_GAME.assertGameState(gameState);
        GameStateEnum.NEW_GAME.assertUserInterfaceState(gameState, callInputLabel, autoButton, callButton, dicesUnderCupActors);

        String logMessageWithState = logMessages.get(1);
        String state = logMessageWithState.substring(GameState.SAVED_GAMESTATE.length());

        // From this point, reset and reload the game.
        gameState.resetToNull();
        when(preferences.getString(GameState.KEY)).thenReturn(state);

        gameState = GameState.get();
        initializeUI(gameState);

        GameStateEnum.NEW_GAME.assertGameState(gameState);
        GameStateEnum.NEW_GAME.assertUserInterfaceState(gameState, callInputLabel, autoButton, callButton, dicesUnderCupActors);
    }

    private UserInterface getTestUserInterface() {
        return new UserInterface() {
            @Override
            public void call(String call) {}

            @Override
            public void finishGame(String winner) {}

            @Override
            public void showTutorialMessage(TutorialMessage message, String... arguments) {}

            @Override
            public void showLockMessage() {}
        };
    }

    private ArrayList<String> loadDefaultPlayers() {
        ArrayList<String> playerList = new ArrayList<>();
        playerList.add("Leon");
        playerList.add("Dirk");
        return playerList;
    }
}
