package net.leejjon.bluffpoker.interfaces;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import net.leejjon.bluffpoker.assets.TextureKey;
import net.leejjon.bluffpoker.state.GameState;

public interface StageInterface {
	void startSelectingPlayersToPlayWith();
	void continuePlaying();
	void openSettingsStage();
	void closeSettingsStage();
	void backToStartStage();
	void startGame(List<String> players);
	GameState getState();
    Texture getTexture(TextureKey textureKey);
    // TODO: add getUISkin method.
}
