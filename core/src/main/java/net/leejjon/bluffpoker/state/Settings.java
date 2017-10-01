package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.Getter;
import net.leejjon.bluffpoker.logic.BluffPokerPreferences;

public class Settings {
	public static final String KEY = "settings";

	@Getter	private int numberOfLives = 3;
	@Getter private boolean allowBok = true;
	@Getter private boolean allowSharedBok = false;
	@Getter private boolean tutorialMode = true;

    private Settings() {}

    public void setNumberOfLives(int numberOfLives) {
        if (this.numberOfLives != numberOfLives) {
            this.numberOfLives = numberOfLives;
            saveSettings();
        }
    }

    public void setAllowBok(boolean allowBok) {
        if (this.allowBok != allowBok) {
            this.allowBok = allowBok;
            saveSettings();
        }
    }

    public void setAllowSharedBok(boolean allowSharedBok) {
        if (this.allowSharedBok != allowSharedBok) {
            this.allowSharedBok = allowSharedBok;
            saveSettings();
        }
    }

    public void setTutorialMode(boolean tutorialMode) {
        if (this.tutorialMode != tutorialMode) {
            this.tutorialMode = tutorialMode;
            saveSettings();
        }
    }

    private void saveSettings() {
        Gson gson = new Gson();
        Preferences preferences = Gdx.app.getPreferences(BluffPokerPreferences.KEY);
        String json = gson.toJson(this);
        preferences.putString(KEY, json);
        preferences.flush();
    }

    public static Settings getSettings() {
        // Load game state if a previous state exists.
        Preferences bluffPokerPreferences = Gdx.app.getPreferences(BluffPokerPreferences.KEY);

        Gson gson = new Gson();
        Settings settings;
        String stateString = bluffPokerPreferences.getString(KEY);
        if (Strings.isNullOrEmpty(stateString)) {
            settings = new Settings();
        } else {
            settings = gson.fromJson(bluffPokerPreferences.getString(KEY), Settings.class);
        }
        return settings;
    }
}
