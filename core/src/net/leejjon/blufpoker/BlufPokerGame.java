package net.leejjon.blufpoker;

import java.util.List;

import net.leejjon.blufpoker.listener.ChangeStageListener;
import net.leejjon.blufpoker.listener.PhoneInputListener;
import net.leejjon.blufpoker.stages.ChoosePlayersStage;
import net.leejjon.blufpoker.stages.GameStage;
import net.leejjon.blufpoker.stages.SettingsStage;
import net.leejjon.blufpoker.stages.StartStage;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BlufPokerGame extends ApplicationAdapter implements
		ChangeStageListener, PhoneInputListener {
	private Skin uiSkin;
	private Settings settings = new Settings();

	private StartStage startMenuStage;
	private ChoosePlayersStage choosePlayerStage;
	private SettingsStage settingsStage;
	private GameStage gameStage;
	
	private int zoomfactor;

	public BlufPokerGame(int zoomfactor) {
		this.zoomfactor = zoomfactor;
	}

	@Override
	public void create() {
		// Use the default libgdx UI skin.
		uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		uiSkin.addRegions(new TextureAtlas("uiskin.atlas"));
		
		// Create the stages.
		startMenuStage = new StartStage(zoomfactor, uiSkin, this);
		settingsStage = new SettingsStage(zoomfactor, uiSkin, this, settings);
		choosePlayerStage = new ChoosePlayersStage(zoomfactor, uiSkin, this);
		gameStage = new GameStage(zoomfactor, uiSkin);
		
		// Make sure touch input goes to the startStage.
		Gdx.input.setInputProcessor(startMenuStage);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Just call the draw methods of the stages. They will figure out by
		// themselves if they need to be drawn or not.
		startMenuStage.draw();
		settingsStage.draw();
		choosePlayerStage.draw();
		gameStage.draw();
	}

	@Override
	public void dispose() {
		startMenuStage.dispose();
		settingsStage.dispose();
		choosePlayerStage.dispose();
		gameStage.dispose();
	}

	@Override
	public void startSelectingPlayersToPlayWith() {
		startMenuStage.setVisible(false);
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
	public void startGame(List<String> players) {
		choosePlayerStage.setVisible(false);
		gameStage.startGame(players, settings);
		gameStage.setVisible(true);
		Gdx.input.setInputProcessor(gameStage);
	}

	@Override
	public void shakePhone() {
		gameStage.playDiceRoll();
	}
}
