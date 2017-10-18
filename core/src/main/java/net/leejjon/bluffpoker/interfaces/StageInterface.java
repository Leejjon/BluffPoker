package net.leejjon.bluffpoker.interfaces;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import net.leejjon.bluffpoker.enums.TextureKey;
import net.leejjon.bluffpoker.state.GameState;
import net.leejjon.bluffpoker.state.Settings;

public interface StageInterface {
	void startSelectingPlayersToPlayWith();
	void continuePlaying();
	void openSettingsStage();
	void closeSettingsStage();
	void backToStartStage();
	void startGame(List<String> players);
	GameState getState();
    Texture getTexture(TextureKey textureKey);
    Settings getSettings();
    // TODO: add getUISkin method.
}
