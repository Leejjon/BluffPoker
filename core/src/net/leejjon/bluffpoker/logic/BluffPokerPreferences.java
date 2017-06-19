package net.leejjon.bluffpoker.logic;

import com.badlogic.gdx.Preferences;

import java.util.Set;

/**
 * LibGDX supports some ancient Android version that did not give support for sets of Strings in the preferences. So I am extending it.
 */
public interface BluffPokerPreferences extends Preferences {
    Preferences putStringSet(String key, Set<String> val);

    Set<String> getStringSet(String key, Set<String> defValue);
}
