package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import net.leejjon.bluffpoker.BluffPokerGame;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GameStateTest extends GdxTest {
    Label thirdLatestOutputLabel;
    Label secondLatestOutputLabel;
    Label latestOutputLabel;

    @Test
    public void testConsoleLogging_threeMessages_success() {
        GameState gameState = GameState.getInstance();

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

    private void initializeUI(GameState gameState) {
        thirdLatestOutputLabel = gameState.createThirdLatestOutputLabel(uiSkin);
        secondLatestOutputLabel = gameState.createSecondLatestOutputLabel(uiSkin);
        latestOutputLabel = gameState.createLatestOutputLabel(uiSkin);
    }

    @Test
    public void testLogging() {
        String logMesssage = "Hoi";
        Gdx.app.log(BluffPokerGame.TAG, logMesssage);

        assertEquals(logMesssage, logMessages.get(0));
    }
}
