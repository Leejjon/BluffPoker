package net.leejjon.blufpoker;

import net.leejjon.blufpoker.listener.ChangeStageListener;
import net.leejjon.blufpoker.stages.ChoosePlayersStage;
import net.leejjon.blufpoker.stages.SettingsStage;
import net.leejjon.blufpoker.stages.StartStage;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BlufPokerGame extends ApplicationAdapter implements
		ChangeStageListener {
	private Skin uiSkin;
	
	private Settings settings = new Settings();

	private StartStage startMenuStage;
	private ChoosePlayersStage choosePlayerStage;
	private SettingsStage settingsStage;
	
	// For debug drawing
	private ShapeRenderer shapeRenderer;
	
	private int zoomfactor;

	private Sound diceRoll;

	public BlufPokerGame(int zoomfactor) {
		this.zoomfactor = zoomfactor;
	}

	@Override
	public void create() {
		// Use the default libgdx UI skin.
		uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		uiSkin.addRegions(new TextureAtlas("uiskin.atlas"));
		
		shapeRenderer = new ShapeRenderer();

		// Create the stages.
		startMenuStage = new StartStage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), zoomfactor, uiSkin, this);
		settingsStage = new SettingsStage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), zoomfactor, uiSkin, this, settings);
		choosePlayerStage = new ChoosePlayersStage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), zoomfactor, uiSkin, this);
		
		// Make sure touch input goes to the startStage.
		Gdx.input.setInputProcessor(startMenuStage);
		diceRoll = Gdx.audio.newSound(Gdx.files.internal("sound/diceroll.mp3"));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Just call the draw methods of the stages. They will figure out by
		// themselves if they need to be drawn or not.
		startMenuStage.draw();
		settingsStage.draw();
		choosePlayerStage.draw();
//		choosePlayerStage.drawDebug(shapeRenderer);
	}

	@Override
	public void dispose() {
		startMenuStage.dispose();
		settingsStage.dispose();
		diceRoll.dispose();
		choosePlayerStage.dispose();
		shapeRenderer.dispose();
	}

	@Override
	public void startSelectingPlayersToPlayWith() {
		startMenuStage.setVisible(false);
		choosePlayerStage.setVisible(true);
		Gdx.input.setInputProcessor(choosePlayerStage);
//		System.out.println(diceRoll.play(1.0f));
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
}
