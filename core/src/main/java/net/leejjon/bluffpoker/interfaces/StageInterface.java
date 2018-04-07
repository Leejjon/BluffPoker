package net.leejjon.bluffpoker.interfaces;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import net.leejjon.bluffpoker.enums.TextureKey;

public interface StageInterface {
	void startSelectingPlayersToPlayWith();
	void continuePlaying();
	void openSettingsStage();
	void closeSettingsStage();
	void backToStartStage();
	void startGame(ArrayList<String> players);
    Texture getTexture(TextureKey textureKey);
    void openPauseScreen(int x);
//	void movePauseScreen(int x);
    void closePauseScreen();
}
