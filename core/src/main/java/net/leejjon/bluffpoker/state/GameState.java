package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.Getter;
import net.leejjon.bluffpoker.logic.BluffPokerPreferences;

public class GameState {
    public static final String KEY = "gameState";

    @Getter
    private transient boolean newGameState = true;

    @Getter
    private transient Label thirdLatestOutputLabel;

    public Label setThirdLatestOutputLabel(Label thirdLatestOutputLabel) {
        this.thirdLatestOutputLabel = thirdLatestOutputLabel;
        this.thirdLatestOutputLabel.setText(thirdLatestOutput);
        return thirdLatestOutputLabel;
    }

    private String thirdLatestOutput = "";

    @Getter
    private transient Label secondLatestOutputLabel;

    public Label setSecondLatestOutputLabel(Label secondLatestOutputLabel) {
        this.secondLatestOutputLabel = secondLatestOutputLabel;
        this.secondLatestOutputLabel.setText(secondLatestOutput);
        return this.secondLatestOutputLabel;
    }

    private String secondLatestOutput = "";

    @Getter
    private transient Label latestOutputLabel;

    public Label setLatestOutputLabel(Label latestOutputLabel) {
        this.latestOutputLabel = latestOutputLabel;
        this.latestOutputLabel.setText(latestOutput);
        return latestOutputLabel;
    }

    private String latestOutput = "";

    public void logGameConsoleMessage(String consoleMessage) {
        thirdLatestOutput = secondLatestOutput;
        secondLatestOutput = latestOutput;
        latestOutput = consoleMessage;
        thirdLatestOutputLabel.setText(thirdLatestOutput);
        secondLatestOutputLabel.setText(secondLatestOutput);
        latestOutputLabel.setText(latestOutput);
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
