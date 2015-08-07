package net.leejjon.blufpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public abstract class AbstractStage extends Stage {
	protected boolean visibility;
	
	protected Table table;
	
	public AbstractStage(int divideScreenByThis, boolean defaultVisibility) {
		super(new StretchViewport(divideScreenByThis > 0 ?  Gdx.graphics.getWidth()/divideScreenByThis :  Gdx.graphics.getWidth(), 
				divideScreenByThis > 0 ? Gdx.graphics.getHeight()/divideScreenByThis : Gdx.graphics.getHeight()));
		visibility = defaultVisibility;
		
		table = new Table();
		table.setFillParent(true);
	}
	
	@Override
	public void draw() {
		act(Gdx.graphics.getDeltaTime());
		if (visibility) {
			super.draw();
		}
	}
	
	public void setVisible(boolean visibility) {
		this.visibility = visibility;
	}
}
