package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.logic.BluffPokerPreferences;

import java.util.ArrayList;

import lombok.Getter;

public class SelectPlayersStageState {
    public static final String KEY = "selectPlayersStageState";

    @Getter
    private transient List<String> playerList;

    public List<String> createPlayerList(List.ListStyle listStyle) {
        playerList = new List<>(listStyle);
        playerList.setItems(players.toArray(new String[players.size()]));
        return playerList;
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

    public void removePlayer(String playerName) {
        players.remove(playerName);
        playerList.setItems(players.toArray(new String[players.size()]));
    }

    private void save() {
        Gson gson = new Gson();
        Preferences preferences = Gdx.app.getPreferences(BluffPokerPreferences.KEY);
        String json = gson.toJson(this);
        preferences.putString(SelectPlayersStageState.KEY, json);
        preferences.flush();
    }

    public static ArrayList<String> updatePlayerListMoveWinnerOnTop(String winner) {
        ArrayList<String> players = SelectPlayersStageState.getInstance().getPlayers();
        players.remove(players.indexOf(winner));
        players.add(0, winner);
        SelectPlayersStageState.getInstance().setPlayers(players);
        return players;
    }

    private SelectPlayersStageState() {
    }

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
            String stateString = bluffPokerState.getString(SelectPlayersStageState.KEY);
            Gdx.app.log(BluffPokerApp.TAG, "Select players stateString: " + stateString);
            if (Strings.isNullOrEmpty(stateString)) {
                instance = new SelectPlayersStageState();
            } else {
                Gson gson = new Gson();
                instance = gson.fromJson(bluffPokerState.getString(SelectPlayersStageState.KEY), SelectPlayersStageState.class);
            }
            return instance;
        }
    }

    /**
     * TEST PURPOSES ONLY.
     */
    static void resetSingletonInstance() {
        instance = null;
    }
}
