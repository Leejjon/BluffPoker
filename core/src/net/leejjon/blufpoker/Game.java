package net.leejjon.blufpoker;

import net.leejjon.blufpoker.Settings;

import java.util.List;

/**
 * Created by Leejjon on 3-10-2015.
 */
public class Game {
    private List<String> players;
    private Settings settings;

    private boolean isAllowedToThrow = false;
    private boolean isAllowedToBelieveOrNotBelieve = false;

    public Game(List<String> players, Settings settings) {
        this.players = players;
        this.settings = settings;
    }
}
