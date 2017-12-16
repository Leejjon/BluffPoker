package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.Getter;
import net.leejjon.bluffpoker.logic.BluffPokerPreferences;

public class GameState {
    public static final String KEY = "gameState";
    // Font used in console is Microsoft JingHei
    public static final String console = "console";

    @Getter
    private transient boolean newGameState = true;

    private void checkUiInitializedPreconditions() {
        Preconditions.checkNotNull(thirdLatestOutputLabel);
        Preconditions.checkNotNull(secondLatestOutputLabel);
        Preconditions.checkNotNull(latestOutputLabel);
    }

    // Stateful UI elements

    String thirdLatestOutput = "";
    private transient Label thirdLatestOutputLabel;

    String secondLatestOutput = "";
    private transient Label secondLatestOutputLabel;

    String latestOutput = "";
    private transient Label latestOutputLabel;

    // UI element initialization methods

    public Label createThirdLatestOutputLabel(Skin uiSkin) {
        thirdLatestOutputLabel = new Label(thirdLatestOutput, uiSkin, console, Color.BLACK);
        thirdLatestOutputLabel.setWrap(true);
        return thirdLatestOutputLabel;
    }

    public Label createSecondLatestOutputLabel(Skin uiSkin) {
        secondLatestOutputLabel = new Label(secondLatestOutput, uiSkin, console, Color.BLACK);
        secondLatestOutputLabel.setWrap(true);
        return this.secondLatestOutputLabel;
    }

    public Label createLatestOutputLabel(Skin uiSkin) {
        latestOutputLabel = new Label(latestOutput, uiSkin, console, Color.BLACK);
        latestOutputLabel.setWrap(true);
        return latestOutputLabel;
    }

    // UI element update methods.

    public void logGameConsoleMessage(String consoleMessage) {
        checkUiInitializedPreconditions();

        thirdLatestOutput = secondLatestOutput;
        secondLatestOutput = latestOutput;
        latestOutput = consoleMessage;
        thirdLatestOutputLabel.setText(thirdLatestOutput);
        secondLatestOutputLabel.setText(secondLatestOutput);
        latestOutputLabel.setText(latestOutput);
        saveGame();
    }

    private GameState() {}

    private void saveGame() {
        newGameState = false;
        save();
    }

    private void save() {
        Gson gson = new Gson();
        Preferences preferences = Gdx.app.getPreferences(BluffPokerPreferences.KEY);
        String json = gson.toJson(this);
        preferences.putString(GameState.KEY, json);
        preferences.flush();
    }

    /**
     * Singleton with lazy initialization.
     */
    private static GameState instance;

    /**
     * @return The only instantiation of the GameState within the app.
     */
    public static synchronized GameState getInstance() {
        if (instance != null) {
            return instance;
        } else {
            // Load game state if a previous state exists.
            Preferences bluffPokerState = Gdx.app.getPreferences(BluffPokerPreferences.KEY);

            Gson gson = new Gson();
            String stateString = bluffPokerState.getString(GameState.KEY);
            if (Strings.isNullOrEmpty(stateString)) {
                instance = new GameState();
            } else {
                instance = gson.fromJson(bluffPokerState.getString(GameState.KEY), GameState.class);
            }
            return instance;
        }
    }
}
