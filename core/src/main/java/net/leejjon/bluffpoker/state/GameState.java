package net.leejjon.bluffpoker.state;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.Getter;
import net.leejjon.bluffpoker.logic.BluffPokerPreferences;

public class GameState {
    public static final String KEY = "gameState";

    @Getter
    private transient boolean newGameState = true;

    private List<String> players = new ArrayList<>();

    private GameState() {}

    public List<String> getPlayers() {
        return new ArrayList<>(players);
    }

    public void setPlayers(List<String> players) {
       players.addAll(players);
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
     * Turn this class into a proper Singleton.
     * @return
     */
    public static GameState getGameState() {
        // Load game state if a previous state exists.
        Preferences bluffPokerState = Gdx.app.getPreferences(BluffPokerPreferences.KEY);

        GameState state;
        Gson gson = new Gson();
        String stateString = bluffPokerState.getString(GameState.KEY);
        if (Strings.isNullOrEmpty(stateString)) {
            state = new GameState();
        } else {
            state = gson.fromJson(bluffPokerState.getString(GameState.KEY), GameState.class);
        }
        return state;
    }
}
