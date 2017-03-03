package net.leejjon.bluffpoker.listener;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import net.leejjon.bluffpoker.assets.TextureKey;
import net.leejjon.bluffpoker.logic.Settings;

public interface StageInterface {
	void startSelectingPlayersToPlayWith();
	void openSettingsStage();
	void closeSettingsStage(Settings newSettings);
	void backToStartStage();
	void startGame(List<String> players);
    Texture getTexture(TextureKey textureKey);
}
