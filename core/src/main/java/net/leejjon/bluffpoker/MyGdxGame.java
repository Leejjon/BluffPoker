package net.leejjon.bluffpoker;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
    BitmapFont bmf;
    Music m;
    Stage startStage;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
        bmf = new BitmapFont();
        m = Gdx.audio.newMusic(Gdx.files.internal("sound/diceroll.mp3"));

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (m.isPlaying()) {
                    m.pause();
                } else {
                    m.play();
                }
                return super.touchUp(screenX, screenY, pointer, button);
            }
        });

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
//		batch.draw(img, 0, 0);
		bmf.draw(batch, String.format("%.2f, %.2f, %.2f", Gdx.input.getAccelerometerX(), Gdx.input.getAccelerometerY(), Gdx.input.getAccelerometerZ()), 10,100);
		batch.end();


	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		startStage.dispose();
		m.dispose();
	}
}
