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
import lombok.Setter;

import net.leejjon.bluffpoker.logic.BluffPokerPreferences;
import net.leejjon.bluffpoker.logic.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * A singleton to contain all the state in the game, designed with testability in mind.
 */
public class GameState {
    public static final String KEY = "gameState";
    // Font used in console is Microsoft JingHei
    public static final String console = "console";

    @Getter
    private boolean newGameState = true;

    private void checkUiInitializedPreconditions() {
        Preconditions.checkNotNull(thirdLatestOutputLabel);
        Preconditions.checkNotNull(secondLatestOutputLabel);
        Preconditions.checkNotNull(latestOutputLabel);
    }

    // Actual state
    @Getter
    private Player[] players;

    @Getter
    private int playerIterator = 0;

    String thirdLatestOutput = "";
    String secondLatestOutput = "";
    String latestOutput = "";

    // Custom state getter methods
    public Player getCurrentPlayer() {
        return players[playerIterator];
    }

    // Stateful UI elements
    private transient Label thirdLatestOutputLabel;
    private transient Label secondLatestOutputLabel;
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
    public void constructPlayers(ArrayList<String> originalPlayers, int numberOfLives) {
        players = new Player[originalPlayers.size()];
        for (int i = 0; i < originalPlayers.size(); i++) {
            players[i] = new Player(originalPlayers.get(i), numberOfLives);
        }
        saveGame();
    }

    public void updatePlayerIterator(int newPlayerIteratorValue) {
        playerIterator = newPlayerIteratorValue;
        saveGame();
    }

    public void currentPlayerLosesLife(boolean canUseBok) {
        getCurrentPlayer().loseLife(canUseBok);
        saveGame();
    }

    public void logGameConsoleMessage(String consoleMessage) {
        checkUiInitializedPreconditions();

        thirdLatestOutput = secondLatestOutput;
        secondLatestOutput = latestOutput;
        latestOutput = consoleMessage;
        updateOutputLabels();
        saveGame();
    }

    private void updateOutputLabels() {
        thirdLatestOutputLabel.setText(thirdLatestOutput);
        secondLatestOutputLabel.setText(secondLatestOutput);
        latestOutputLabel.setText(latestOutput);
    }

    private GameState() {}

    private GameState(Label thirdLatestOutputLabel, Label secondLatestOutputLabel, Label latestOutputLabel) {
        this.thirdLatestOutputLabel = thirdLatestOutputLabel;
        this.secondLatestOutputLabel = secondLatestOutputLabel;
        this.latestOutputLabel = latestOutputLabel;
        updateOutputLabels();
        save();
    }

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

    public static synchronized void resetInstance() {
        instance = new GameState(instance.thirdLatestOutputLabel, instance.secondLatestOutputLabel, instance.latestOutputLabel);
    }
}
