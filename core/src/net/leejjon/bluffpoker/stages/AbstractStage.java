package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import net.leejjon.bluffpoker.BlufPokerGame;

public abstract class AbstractStage extends Stage {
	protected boolean visible;
	
	protected Table table;

	public AbstractStage(boolean defaultVisibility) {
		super(new StretchViewport(BlufPokerGame.getDivideScreenByThis() > 0 ?  Gdx.graphics.getWidth() / BlufPokerGame.getDivideScreenByThis() :  Gdx.graphics.getWidth(),
				BlufPokerGame.getDivideScreenByThis() > 0 ? Gdx.graphics.getHeight()/ BlufPokerGame.getDivideScreenByThis() : Gdx.graphics.getHeight()));
		visible = defaultVisibility;
		
		table = new Table();
		table.setFillParent(true);
	}
	
	@Override
	public void draw() {
		act(Gdx.graphics.getDeltaTime());
		if (visible) {
			super.draw();
		}
	}

	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
