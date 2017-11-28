package net.leejjon.bluffpoker.interfaces;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import net.leejjon.bluffpoker.enums.TextureKey;

public interface StageInterface {
	void startSelectingPlayersToPlayWith();
	void continuePlaying();
	void openSettingsStage();
	void closeSettingsStage();
	void backToStartStage();
	void startGame(List<String> players);
    Texture getTexture(TextureKey textureKey);
    // TODO: add getUISkin method.
}
