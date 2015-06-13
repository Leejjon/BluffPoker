package net.leejjon.blufpoker;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class StartStage extends Stage {
	private Table table;
	private Label titleLabel;
	
	public StartStage(float w, float h, Skin uiSkin, int divideScreenByThis) {
		super(new StretchViewport(divideScreenByThis > 0 ? w/divideScreenByThis : w, divideScreenByThis > 0 ? h/divideScreenByThis : h));
		
		table = new Table();
		table.setFillParent(true);
		table.center();
		
		titleLabel = new Label("Bluf Poker", uiSkin);
		titleLabel.setColor(Color.WHITE);
		
		table.add(titleLabel);
		
		addActor(table);
	}
	
	
}
