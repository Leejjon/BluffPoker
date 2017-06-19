package net.leejjon.bluffpoker.android;

import android.content.SharedPreferences;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidPreferences;
import net.leejjon.bluffpoker.logic.BluffPokerPreferences;

import java.util.Set;

public class BluffPokerAndroidPrerefences extends AndroidPreferences implements BluffPokerPreferences {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public BluffPokerAndroidPrerefences(SharedPreferences preferences) {
        super(preferences);
        this.preferences = preferences;
    }

    @Override
    public Preferences putStringSet(String key, Set<String> val) {
        if (editor == null) {
            editor = preferences.edit();
        }
        editor.putStringSet(key, val);
        return this;
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defValue) {
        return preferences.getStringSet(key, defValue);
    }
}
