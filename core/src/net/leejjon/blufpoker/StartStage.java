package net.leejjon.blufpoker;

import net.leejjon.blufpoker.listener.ChangeStageListener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class StartStage extends Stage {
	private Table table;
	private TextButton playButton;
	private final ChangeStageListener changeScreen;
	
	public StartStage(float w, float h, Skin uiSkin, int divideScreenByThis, final ChangeStageListener changeScreen) {
		super(new StretchViewport(divideScreenByThis > 0 ? w/divideScreenByThis : w, divideScreenByThis > 0 ? h/divideScreenByThis : h));
		this.changeScreen = changeScreen;
		
		table = new Table();
		table.setFillParent(true);
		table.center();
		
		Label titleLabel = new Label("Bluf Poker", uiSkin);
		titleLabel.setColor(Color.WHITE);
		
		playButton = new TextButton("Play", uiSkin);
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				changeScreen.startGame();
			}
		});
		
		table.add(titleLabel);
		table.row();
		table.row();
		table.add(playButton);
		table.row();
		table.row();
		
		addActor(table);
	}
	
	
}
