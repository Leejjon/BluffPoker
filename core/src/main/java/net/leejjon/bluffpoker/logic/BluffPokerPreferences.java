package net.leejjon.bluffpoker.logic;

import com.badlogic.gdx.Preferences;
import net.leejjon.bluffpoker.state.GameState;

/**
 * Extended the libGDX preferences class to be able to store the state in json.
 */
public interface BluffPokerPreferences extends Preferences {
    public static final String KEY = "bluffpoker";

    Preferences putState(String key, GameState val);

    GameState getState(String key, GameState defValue);
}
