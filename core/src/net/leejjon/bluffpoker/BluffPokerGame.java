package net.leejjon.bluffpoker;

import java.util.List;

import net.leejjon.bluffpoker.listener.ChangeStageListener;
import net.leejjon.bluffpoker.listener.PhoneInputListener;
import net.leejjon.bluffpoker.logic.Settings;
import net.leejjon.bluffpoker.stages.ChoosePlayersStage;
import net.leejjon.bluffpoker.stages.GameStage;
import net.leejjon.bluffpoker.stages.SettingsStage;
import net.leejjon.bluffpoker.stages.StartStage;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BluffPokerGame extends ApplicationAdapter implements
		ChangeStageListener, PhoneInputListener {
	private Skin uiSkin;
	private Settings settings = new Settings();

	private StartStage startMenuStage;
	private ChoosePlayersStage choosePlayerStage;
	private SettingsStage settingsStage;
	private GameStage gameStage;
	
	private static int divideScreenByThis;

	public BluffPokerGame(int divideScreenByThis) {
		this.divideScreenByThis = divideScreenByThis;
	}

	@Override
	public void create() {
		// Use the default libgdx UI skin.
		uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		uiSkin.addRegions(new TextureAtlas("uiskin.atlas"));

		// Create the stages.
		startMenuStage = new StartStage(uiSkin, this);
		settingsStage = new SettingsStage(uiSkin, this, settings);
		choosePlayerStage = new ChoosePlayersStage(uiSkin, this);
		gameStage = new GameStage(uiSkin, this);

		// Make sure touch input goes to the startStage.
		Gdx.input.setInputProcessor(startMenuStage);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Just validateCall the draw methods of the stages. They will figure out by
		// themselves if they need to be drawn or not.
		startMenuStage.draw();
		settingsStage.draw();
		choosePlayerStage.draw();
		gameStage.act();
		gameStage.draw();
	}

	@Override
	public void dispose() {
		startMenuStage.dispose();
		settingsStage.dispose();
		choosePlayerStage.dispose();
		gameStage.dispose();
		uiSkin.dispose();
	}

	@Override
	public void startSelectingPlayersToPlayWith() {
		startMenuStage.setVisible(false);
		choosePlayerStage.clearCurrentPlayers();
		choosePlayerStage.setVisible(true);
		Gdx.input.setInputProcessor(choosePlayerStage);
	}

	@Override
	public void openSettingsStage() {
		startMenuStage.setVisible(false);
		settingsStage.loadLatestSettings(settings);
		settingsStage.setVisible(true);
		Gdx.input.setInputProcessor(settingsStage);
	}

	@Override
	public void closeSettingsStage(Settings settings) {
		this.settings = settings;
		settingsStage.setVisible(false);
		startMenuStage.setVisible(true);
		Gdx.input.setInputProcessor(startMenuStage);
	}

	@Override
	public void backToStartStage() {
		gameStage.setVisible(false);
		startMenuStage.setVisible(true);
		Gdx.input.setInputProcessor(startMenuStage);
	}

	@Override
	public void startGame(List<String> players) {
		choosePlayerStage.setVisible(false);
		gameStage.startGame(players, settings);
		gameStage.setVisible(true);
		Gdx.input.setInputProcessor(gameStage);
	}

	@Override
	public void shakePhone() {
		if (gameStage.isVisible()) {
            gameStage.shake();
        }
	}

    public static int getDivideScreenByThis() {
        return divideScreenByThis;
    }
}
