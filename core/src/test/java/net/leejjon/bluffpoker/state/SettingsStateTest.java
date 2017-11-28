package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import net.leejjon.bluffpoker.BluffPokerGame;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SettingsStateTest {
    @Mock
    private Application app;

    @Mock
    private Preferences preferences;

    @Before
    public void setUp() {
        Gdx.app = app;
    }

    @Test
    public void testRetrievingSettings_invalidJson_logExceptionAndReturnDefaultSettings() {
        when(preferences.getString(SettingsState.KEY)).thenReturn("somestuff");
        SettingsState settings = SettingsState.getInstance(preferences);

        verify(app, times(1)).log(any(), any(), any());

        assertEquals(3, settings.getNumberOfLives());
        assertEquals(true, settings.isAllowBok());
        assertEquals(false, settings.isAllowSharedBok());
        assertEquals(true, settings.isTutorialMode());
    }
}
