package net.leejjon.blufpoker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class DebugTestGame {
	private Skin uiSkin;
	private Stage stage;
	private Table table;
	// For debug drawing
	private ShapeRenderer shapeRenderer;

	public void create () {
	    stage = new Stage();
	    Gdx.input.setInputProcessor(stage);

	    table = new Table();
	    table.setFillParent(true);
	    stage.addActor(table);

	    shapeRenderer = new ShapeRenderer();

	    // Add widgets to the table here.
	    // Use the default libgdx UI skin.
 		uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
 		uiSkin.addRegions(new TextureAtlas("uiskin.atlas"));
	}

	public void resize (int width, int height) {
	    stage.getViewport().update(width, height, true);
	}

	public void render () {
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    stage.act(Gdx.graphics.getDeltaTime());
	    stage.draw();

	    table.drawDebug(shapeRenderer); // This is optional, but enables debug lines for tables.
	}

	public void dispose() {
	    stage.dispose();
	    shapeRenderer.dispose();
	}
	
}
