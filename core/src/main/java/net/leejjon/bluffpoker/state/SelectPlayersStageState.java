package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.Getter;
import net.leejjon.bluffpoker.logic.BluffPokerPreferences;

import java.util.ArrayList;

public class SelectPlayersStageState {
    private static final String KEY = "selectPlayersStageState";

    @Getter private transient List<String> playerList;

    public void setPlayerList(List<String> playerList) {
        this.playerList = playerList;
        playerList.setItems(players.toArray(new String[players.size()]));
    }

    private ArrayList<String> players = new ArrayList<>();

    public ArrayList<String> getPlayers() {
        return new ArrayList<>(players);
    }

    public void setPlayers(ArrayList<String> players) {
        playerList.setItems(players.toArray(new String[players.size()]));
        this.players = new ArrayList<>(players);
        save();
    }

    private void save() {
        Gson gson = new Gson();
        Preferences preferences = Gdx.app.getPreferences(BluffPokerPreferences.KEY);
        String json = gson.toJson(this);
        preferences.putString(SelectPlayersStageState.KEY, json);
        preferences.flush();
    }

    private SelectPlayersStageState() {}

    /**
     * Singleton with lazy initialization.
     */
    private static SelectPlayersStageState instance;

    /**
     * @return The only instantiation of the GameState within the app.
     */
    public static synchronized SelectPlayersStageState getInstance() {
        if (instance != null) {
            return instance;
        } else {
            // Load game state if a previous state exists.
            Preferences bluffPokerState = Gdx.app.getPreferences(BluffPokerPreferences.KEY);

            Gson gson = new Gson();
            String stateString = bluffPokerState.getString(SelectPlayersStageState.KEY);
            if (Strings.isNullOrEmpty(stateString)) {
                instance = new SelectPlayersStageState();
            } else {
                instance = gson.fromJson(bluffPokerState.getString(SelectPlayersStageState.KEY), SelectPlayersStageState.class);
            }
            return instance;
        }
    }
}
