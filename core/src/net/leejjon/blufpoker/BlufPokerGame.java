package net.leejjon.blufpoker;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BlufPokerGame extends ApplicationAdapter {
	Skin uiSkin;
	
	SpriteBatch batch;
	Texture img;
	BitmapFont font;
	
	Stage currentStage;
	
	StartStage startStage;
	
	int zoomfactor;
	
	public BlufPokerGame(int zoomfactor) {
		this.zoomfactor = zoomfactor;
	}
	
	@Override
	public void create () {
		// Use the default libgdx UI skin.
		uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		uiSkin.addRegions(new TextureAtlas("uiskin.atlas"));
		
		
//		startStage = new StartStage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), uiSkin, zoomfactor);
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		font = new BitmapFont();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
//		startStage.act(Gdx.graphics.getDeltaTime());
//	    startStage.draw();
		
		batch.begin();
		batch.draw(img, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		font.setColor(0.0f, 0.0f, 1.0f, 1.0f); // tint font blue
		font.draw(batch, "Test", 0, 20);
		batch.end();
	}
	
	@Override
	public void dispose() {
//		startStage.dispose();
		batch.dispose();
		img.dispose();
		font.dispose();
	}
}
