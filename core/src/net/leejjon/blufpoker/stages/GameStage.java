package net.leejjon.blufpoker.stages;

import java.util.List;

import net.leejjon.blufpoker.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameStage extends AbstractStage {
	private int width = 0;
	private int height = 0;
	private int tw = 0;
	private int th = 0;
	
	private Settings settings = null;
	private Texture cupTexture;
	private SpriteBatch batch;
	
	public GameStage(int divideScreenByThis, Skin uiSkin) {
		super(divideScreenByThis, false);
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		
		
		cupTexture = new Texture("data/ingamebeker.png");
		tw = cupTexture.getWidth();
		th = cupTexture.getHeight();
		
		batch = new SpriteBatch();
	}
	
	public void startGame(List<String> players, Settings settings) {
		this.settings = settings;
	}

	@Override
	public void draw() {
		if (visibility) {
			batch.begin();
			batch.draw(cupTexture, getMiddleX()-(tw/2), getMiddleY()-(th/2));
			batch.end();
		}
		super.draw();
	}
	
	private int getMiddleX() {
		return (width/2);
	}
	private int getMiddleY() {
		return (height/2);
	}
	
	public void dispose() {
		batch.dispose();
		cupTexture.dispose();
		super.dispose();
	}
}
