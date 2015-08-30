package net.leejjon.blufpoker.stages;

import java.util.List;

import net.leejjon.blufpoker.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameStage extends AbstractStage implements GestureListener {
	private int width = 0;
	private int height = 0;
	private int tw = 0;
	private int th = 0;
	
	private Settings settings = null;
	private Texture cupTexture;
	private SpriteBatch batch;
	private Sound diceRoll;
	
	public GameStage(int divideScreenByThis, Skin uiSkin) {
		super(divideScreenByThis, false);
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		
		
		cupTexture = new Texture("data/ingamebeker.png");
		tw = cupTexture.getWidth();
		th = cupTexture.getHeight();
		
		batch = new SpriteBatch();
		diceRoll = Gdx.audio.newSound(Gdx.files.internal("sound/diceroll.mp3"));
	}
	
	public void startGame(List<String> players, Settings settings) {
		this.settings = settings;
	}

	@Override
	public void draw() {
		if (visibility) {
			batch.begin();
			batch.draw(cupTexture, getMiddleX()-(getCupWidth()/2), getMiddleY()-(getCupHeight()/2), getCupWidth(), getCupHeight());
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
	
	private int getCupWidth() {
		return tw/2;
	}
	
	private int getCupHeight() {
		return th/2;
	}
	
	public void playDiceRoll() {
		if (visibility) {
			diceRoll.play(1.0f);
		}
	}
	
	public void dispose() {
		batch.dispose();
		cupTexture.dispose();
		diceRoll.dispose();
		super.dispose();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return true;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return true;
	}

	@Override
	public boolean longPress(float x, float y) {
		return true;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return true;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return true;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return true;
	}
}
