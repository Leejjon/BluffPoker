package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.Getter;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.logic.BluffPokerPreferences;

public class SettingsState {
	public static final String KEY = "settings";

    @Getter private transient CheckBox allowBokCheckBox;

    public void setAllowBokCheckBox(CheckBox allowBokCheckBox) {
        this.allowBokCheckBox = allowBokCheckBox;
        this.allowBokCheckBox.setChecked(allowBok);
        this.allowBokCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                allowBok = allowBokCheckBox.isChecked();
                saveSettings();
            }
        });
    }

    @Getter private transient CheckBox allowSharedBokCheckbox;

    public void setAllowSharedBokCheckbox(CheckBox allowSharedBokCheckbox) {
        this.allowSharedBokCheckbox = allowSharedBokCheckbox;
        this.allowSharedBokCheckbox.setChecked(allowSharedBok);
        this.allowSharedBokCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                allowSharedBok = allowSharedBokCheckbox.isChecked();
                saveSettings();
            }
        });
    }

    @Getter private transient Slider numberOfLivesSlider;

    public void setNumberOfLivesSlider(Slider numberOfLivesSlider) {
        this.numberOfLivesSlider = numberOfLivesSlider;
        this.numberOfLivesSlider.setValue(numberOfLives);
        this.numberOfLivesSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setNumberOfLives((int) numberOfLivesSlider.getValue());
                updateActualNumberOfLivesLabel();
            }
        });
    }

    @Getter private transient Label actualNumberOfLivesDisplayLabel;

    public void setActualNumberOfLivesDisplayLabel(Label actualNumberOfLivesDisplayLabel) {
        this.actualNumberOfLivesDisplayLabel = actualNumberOfLivesDisplayLabel;
        updateActualNumberOfLivesLabel();
    }

    private void updateActualNumberOfLivesLabel() {
        this.actualNumberOfLivesDisplayLabel.setText(numberOfLives + "");
    }

    @Getter private transient CheckBox tutorialModeCheckbox;

    public void setTutorialModeCheckbox(CheckBox tutorialModeCheckbox) {
        this.tutorialModeCheckbox = tutorialModeCheckbox;
        this.tutorialModeCheckbox.setChecked(tutorialMode);
        this.tutorialModeCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setTutorialMode(tutorialModeCheckbox.isChecked());
            }
        });
    }

	@Getter private boolean allowBok = true;
	@Getter private boolean allowSharedBok = false;
	@Getter	private int numberOfLives = 3;
	@Getter private boolean tutorialMode = true;

    public void setNumberOfLives(int numberOfLives) {
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
        if (tutorialModeCheckbox.isChecked() != tutorialMode) {
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
            Gdx.app.log(BluffPokerGame.TAG, stateString);
            if (Strings.isNullOrEmpty(stateString)) {
                instance = new SettingsState();
            } else {
                try {
                    Gson gson = new Gson();
                    instance = gson.fromJson(bluffPokerPreferences.getString(KEY), SettingsState.class);
                } catch (JsonSyntaxException e) {
                    Gdx.app.log(BluffPokerGame.TAG, "String that contained save was invalid.", e);
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
