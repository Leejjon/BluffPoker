package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.test.GdxTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SettingsStateTest extends GdxTest {
    private static final String DEFAULT_SETTINGS_TUTORIAL_MODE_OFF = "{\"allowBok\":true,\"allowSharedBok\":false,\"numberOfLives\":3,\"tutorialMode\":false}";

    @Before
    public void setUp() {
        SettingsState.resetSingletonInstance();
    }

    @Test
    public void testRetrievingSettings_validJson_parseSuccesful() {
        when(preferences.getString(SettingsState.KEY)).thenReturn(DEFAULT_SETTINGS_TUTORIAL_MODE_OFF);

        SettingsState settings = SettingsState.getInstance();

        assertEquals(0, logMessages.size);
        assertDefaultSettingsWithTutorialModeOff(settings);
    }

    @Test
    public void testRetrievingSettings_invalidJson_logExceptionAndReturnDefaultSettings() {
        when(preferences.getString(SettingsState.KEY)).thenReturn("some invalid state");

        SettingsState settings = SettingsState.getInstance();

        assertEquals(1, logMessages.size);
        assertEquals(SettingsState.INVALID_SETTINGS, logMessages.get(0));
        assertDefaultSettings(settings);
    }

    @Test
    public void testEnablingTutorialModeInDefaultSingletonWith_success() {
        when(preferences.getString(SettingsState.KEY)).thenReturn(DEFAULT_SETTINGS_TUTORIAL_MODE_OFF);

        SettingsState settings = SettingsState.getInstance();
        CheckBox tutorialModeCheckbox = settings.createTutorialModeCheckbox(uiSkin);
        assertFalse(tutorialModeCheckbox.isChecked());

        // Enabling tutorial mode (as happens during the game).
        settings.setTutorialMode(true);

        // Verify that the change is being put through the UI, state and stored in the preferences.
        verify(preferences, times(2)).putString(any(), any());
        verify(preferences, times(2)).flush();
        assertTrue(tutorialModeCheckbox.isChecked());
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
