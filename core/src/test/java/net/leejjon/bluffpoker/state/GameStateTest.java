package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Gdx;
import net.leejjon.bluffpoker.BluffPokerGame;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameStateTest extends GdxTest {
    @Test
    public void testLogging() {
        Gdx.app.log(BluffPokerGame.TAG, "Hoi");
    }
}
