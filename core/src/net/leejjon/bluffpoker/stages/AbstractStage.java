package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import net.leejjon.bluffpoker.BluffPokerGame;

public abstract class AbstractStage extends Stage {
	protected boolean visible;
	
	protected Table table;

	public AbstractStage(boolean defaultVisibility) {
		super(new StretchViewport(BluffPokerGame.getDivideScreenByThis() > 0 ?  Gdx.graphics.getWidth() / BluffPokerGame.getDivideScreenByThis() :  Gdx.graphics.getWidth(),
				BluffPokerGame.getDivideScreenByThis() > 0 ? Gdx.graphics.getHeight()/ BluffPokerGame.getDivideScreenByThis() : Gdx.graphics.getHeight()));
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
