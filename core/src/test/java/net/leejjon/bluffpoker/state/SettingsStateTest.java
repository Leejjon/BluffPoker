package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import net.leejjon.bluffpoker.BluffPokerGame;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SettingsStateTest {
    private static final String DEFAULT_SETTINGS_TUTORIAL_MODE_OFF = "{\"allowBok\":true,\"allowSharedBok\":false,\"numberOfLives\":3,\"tutorialMode\":false}";

    @Mock
    private Application app;

    @Mock
    private Preferences preferences;

    @Before
    public void setUp() {
        SettingsState.resetSingletonInstance();
        Gdx.app = app;
    }

    @Test
    public void testRetrievingSettings_validJson_parseSuccesful() {
        when(app.getPreferences(BluffPokerGame.TAG)).thenReturn(preferences);
        when(preferences.getString(SettingsState.KEY)).thenReturn(DEFAULT_SETTINGS_TUTORIAL_MODE_OFF);

        SettingsState settings = SettingsState.getInstance();

        verify(app, times(0)).log(any(), any(), any());
        assertDefaultSettingsWithTutorialModeOff(settings);
    }

    @Test
    public void testRetrievingSettings_invalidJson_logExceptionAndReturnDefaultSettings() {
        when(app.getPreferences(BluffPokerGame.TAG)).thenReturn(preferences);
        when(preferences.getString(SettingsState.KEY)).thenReturn("some invalid state");

        SettingsState settings = SettingsState.getInstance();

        verify(app, times(1)).log(any(), any(), any());
        assertDefaultSettings(settings);
    }

    @Test
    public void testEnablingTutorialModeInDefaultSingletonWith_success() {
        when(app.getPreferences(BluffPokerGame.TAG)).thenReturn(preferences);
        when(preferences.getString(SettingsState.KEY)).thenReturn(DEFAULT_SETTINGS_TUTORIAL_MODE_OFF);

        CheckBox tutorialMode = mock(CheckBox.class);
        SettingsState settings = SettingsState.getInstance();
        settings.setTutorialModeCheckbox(tutorialMode);

        verify(tutorialMode, times(1)).setChecked(false);
        verify(tutorialMode, times(0)).setChecked(true);
        verify(tutorialMode, times(1)).addListener(any());
        assertFalse(settings.getTutorialModeCheckbox().isChecked());

        // Enabling tutorial mode (as happens during the game).
        settings.setTutorialMode(true);

        // Verify that the change is being put through the UI, state and stored in the preferences.
        verify(tutorialMode, times(1)).setChecked(true);
        verify(preferences, times(1)).putString(any(), any());
        verify(preferences, times(1)).flush();
        assertTrue(settings.isTutorialMode());
    }

    private void assertDefaultSettings(SettingsState settings) {
        assertEquals(3, settings.getNumberOfLives());
        assertTrue(settings.isAllowBok());
        assertFalse(settings.isAllowSharedBok());
        assertTrue(settings.isTutorialMode());
    }

    private void assertDefaultSettingsWithTutorialModeOff(SettingsState settings) {
        assertEquals(3, settings.getNumberOfLives());
        assertTrue(settings.isAllowBok());
        assertFalse(settings.isAllowSharedBok());
        assertFalse(settings.isTutorialMode());
    }
}
