package net.leejjon.bluffpoker.stages;

import net.leejjon.bluffpoker.interfaces.StageInterface;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class StartStage extends AbstractStage {
	public StartStage(Skin uiSkin, final StageInterface stageInterface) {
		super(true);
		
		Label titleLabel = new Label("Bluff Poker", uiSkin);
		titleLabel.setColor(Color.WHITE);

		TextButton continueButton = new TextButton("Continue", uiSkin);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stageInterface.continuePlaying();
            }
        });

		TextButton playButton = new TextButton("Start", uiSkin);
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stageInterface.startSelectingPlayersToPlayWith();
			}
		});
		
		TextButton settingsButton = new TextButton("Settings", uiSkin);
		settingsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stageInterface.openSettingsStage();
			}
		});
		
		// Create a table and add the ui components to it.
		table.center();
		table.add(titleLabel).padBottom(10f);

		if (!stageInterface.getState().isNewGameState()) {
            table.row();
            table.add(continueButton).padBottom(10f);
        }

		table.row();
		table.add(playButton).padBottom(10f);
		table.row();
		table.add(settingsButton);
		
		addActor(table);
	}
}
