package net.leejjon.blufpoker;

import net.leejjon.blufpoker.listener.ChangeStageListener;
import net.leejjon.blufpoker.stages.SettingsStage;
import net.leejjon.blufpoker.stages.StartStage;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BlufPokerGame extends ApplicationAdapter implements
		ChangeStageListener {
	Skin uiSkin;

	// SpriteBatch batch;
	// Texture img;
	// BitmapFont font;

	Stage currentStage;

	StartStage startStage;
	SettingsStage settingsStage;

	int zoomfactor;

	Sound diceRoll;

	public BlufPokerGame(int zoomfactor) {
		this.zoomfactor = zoomfactor;
	}

	@Override
	public void create() {
		// Use the default libgdx UI skin.
		uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		uiSkin.addRegions(new TextureAtlas("uiskin.atlas"));

		// Create the stages.
		startStage = new StartStage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), zoomfactor, uiSkin, this);
		settingsStage = new SettingsStage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), zoomfactor, uiSkin, this);

		// Make sure touch input goes to the startStage.
		Gdx.input.setInputProcessor(startStage);
		diceRoll = Gdx.audio.newSound(Gdx.files.internal("sound/diceroll.mp3"));

		// batch = new SpriteBatch();
		// img = new Texture("badlogic.jpg");
		// font = new BitmapFont();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Just call the draw methods of the stages. They will figure out by
		// themselves if they need to be drawn or not.
		startStage.draw();
		settingsStage.draw();
		
		// batch.begin();
		// batch.draw(img, Gdx.graphics.getWidth()/2,
		// Gdx.graphics.getHeight()/2);
		// font.setColor(0.0f, 0.0f, 1.0f, 1.0f); // tint font blue
		// font.draw(batch, "Test", 0, 20);
		// batch.end();
	}

	@Override
	public void dispose() {
		startStage.dispose();
		settingsStage.dispose();
		diceRoll.dispose();
		// batch.dispose();
		// img.dispose();
		// font.dispose();
	}

	@Override
	public void startGame() {
		startStage.setVisible(false);
		System.out.println(diceRoll.play(1.0f));
	}

	@Override
	public void openSettingsStage() {
		startStage.setVisible(false);
		settingsStage.setVisible(true);
		Gdx.input.setInputProcessor(settingsStage);
	}
}
