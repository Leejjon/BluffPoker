package net.leejjon.blufpoker.stages;

import net.leejjon.blufpoker.listener.ChangeStageListener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class StartStage extends AbstractStage {
	public StartStage(Skin uiSkin, final ChangeStageListener changeScreen) {
		super(true);
		
		Label titleLabel = new Label("Bluf Poker", uiSkin);
		titleLabel.setColor(Color.WHITE);
		
		TextButton playButton = new TextButton("Play", uiSkin);
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeScreen.startSelectingPlayersToPlayWith();
			}
		});
		
		TextButton settingsButton = new TextButton("Settings", uiSkin);
		settingsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeScreen.openSettingsStage();
			}
		});
		
		// Create a table and add the ui components to it.
		table.center();
		table.add(titleLabel).padBottom(10f);
		table.row();
		table.add(playButton).padBottom(10f);
		table.row();
		table.add(settingsButton);
		
		addActor(table);
	}
}
