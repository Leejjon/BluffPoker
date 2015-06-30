package net.leejjon.blufpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public abstract class AbstractStage extends Stage {
	private boolean visibility;
	
	public AbstractStage(float w, float h, int divideScreenByThis, boolean defaultVisibility) {
		super(new StretchViewport(divideScreenByThis > 0 ? w/divideScreenByThis : w, divideScreenByThis > 0 ? h/divideScreenByThis : h));
		visibility = defaultVisibility;
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
