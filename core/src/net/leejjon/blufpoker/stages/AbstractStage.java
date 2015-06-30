package net.leejjon.blufpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public abstract class AbstractStage extends Stage {
	private boolean visibility;
	
	protected Table table;
	
	public AbstractStage(float w, float h, int divideScreenByThis, boolean defaultVisibility) {
		super(new StretchViewport(divideScreenByThis > 0 ? w/divideScreenByThis : w, divideScreenByThis > 0 ? h/divideScreenByThis : h));
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
