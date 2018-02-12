package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.Getter;
import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.logic.BluffPokerPreferences;

public class SettingsState {
    static final String INVALID_SETTINGS = "String that contained save was invalid.";
	public static final String KEY = "settings";

    public CheckBox createAllowBokCheckBox(Skin uiSkin) {
        CheckBox allowBokCheckBox = new CheckBox("Allow bok", uiSkin);
        allowBokCheckBox.setChecked(allowBok);
        allowBokCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                allowBok = allowBokCheckBox.isChecked();
                saveSettings();
            }
        });
        return allowBokCheckBox;
    }

    public CheckBox createAllowSharedBokCheckbox(Skin uiSkin) {
        CheckBox allowSharedBokCheckbox = new CheckBox("Allow shared bok", uiSkin);
        allowSharedBokCheckbox.setChecked(allowSharedBok);
        allowSharedBokCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                allowSharedBok = allowSharedBokCheckbox.isChecked();
                saveSettings();
            }
        });
        return allowSharedBokCheckbox;
    }

    public Slider createNumberOfLivesSlider(Skin uiSkin) {
        Slider numberOfLivesSlider = new Slider(1f, 10f, 1f, false, uiSkin);
        numberOfLivesSlider.setValue(numberOfLives);
        numberOfLivesSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setNumberOfLives((int) numberOfLivesSlider.getValue());
                updateActualNumberOfLivesLabel();
            }
        });
        return numberOfLivesSlider;
    }

    private transient Label actualNumberOfLivesDisplayLabel;

    public Label createActualNumberOfLivesDisplayLabel(Skin uiSkin) {
        actualNumberOfLivesDisplayLabel = new Label("", uiSkin);
        actualNumberOfLivesDisplayLabel.setColor(Color.WHITE);
        updateActualNumberOfLivesLabel();
        return actualNumberOfLivesDisplayLabel;
    }

    private void updateActualNumberOfLivesLabel() {
        this.actualNumberOfLivesDisplayLabel.setText(numberOfLives + "");
    }

    @Getter private transient CheckBox tutorialModeCheckbox;

    public CheckBox createTutorialModeCheckbox(Skin uiSkin) {
        tutorialModeCheckbox = new CheckBox("Tutorial mode", uiSkin);
        tutorialModeCheckbox.setChecked(tutorialMode);
        tutorialModeCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setTutorialMode(tutorialModeCheckbox.isChecked());
            }
        });
        return tutorialModeCheckbox;
    }

	@Getter private boolean allowBok = true;
	@Getter private boolean allowSharedBok = false;
	@Getter	private int numberOfLives = 3;
	@Getter private boolean tutorialMode = true;

    private void setNumberOfLives(int numberOfLives) {
        if (this.numberOfLives != numberOfLives) {
            this.numberOfLives = numberOfLives;
            saveSettings();
        }
    }

    /**
     * This setter had to be public, because tutorial mode can be turned off during the game.
     * @param tutorialMode
     */
    public void setTutorialMode(boolean tutorialMode) {
        this.tutorialMode = tutorialMode;
        if (tutorialModeCheckbox != null && tutorialModeCheckbox.isChecked() != tutorialMode) {
            tutorialModeCheckbox.setChecked(tutorialMode);
        }
        saveSettings();
    }

    private void saveSettings() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        Preferences preferences = Gdx.app.getPreferences(BluffPokerPreferences.KEY);
        preferences.putString(KEY, json);
        preferences.flush();
    }

    private SettingsState() {}

    /**
     * Singleton with lazy initialization.
     */
    private static SettingsState instance;

    public static synchronized SettingsState getInstance() {
        if (instance == null) {
            // Load game state if a previous state exists.
            Preferences bluffPokerPreferences = Gdx.app.getPreferences(BluffPokerPreferences.KEY);
            String stateString = bluffPokerPreferences.getString(KEY);
            if (Strings.isNullOrEmpty(stateString)) {
                instance = new SettingsState();
            } else {
                try {
                    Gson gson = new Gson();
                    instance = gson.fromJson(bluffPokerPreferences.getString(KEY), SettingsState.class);
                } catch (JsonSyntaxException e) {
                    Gdx.app.log(BluffPokerApp.TAG, INVALID_SETTINGS, e);
                    instance = new SettingsState();
                }
            }
        }
        return instance;
    }

    /**
     * TEST PURPOSES ONLY.
     */
    static void resetSingletonInstance() {
        instance = null;
    }
}
