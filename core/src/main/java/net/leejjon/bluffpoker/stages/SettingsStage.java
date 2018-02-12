package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import net.leejjon.bluffpoker.state.SettingsState;
import net.leejjon.bluffpoker.interfaces.StageInterface;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingsStage extends AbstractStage {
	private SettingsState settingsState;
	
	public SettingsStage(Skin uiSkin, final StageInterface stageInterface) {
		super(false);
		this.settingsState = SettingsState.getInstance();

		Label titleLabel = new Label("SettingsState", uiSkin);
		titleLabel.setColor(Color.WHITE);

		Label numberOfLivesLabel = new Label("Number of lives: ", uiSkin);
		numberOfLivesLabel.setColor(Color.WHITE);

		TextButton backButton = new TextButton("Back", uiSkin);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stageInterface.closeSettingsStage();
			}
		});
		
		// Create a table and add the ui components to it.
		table.center();
		table.add(titleLabel);
		table.row();
		
		Table innerTable = new Table(uiSkin);
		innerTable.setFillParent(false);
		innerTable.row();
		innerTable.add(settingsState.createAllowBokCheckBox(uiSkin)).colspan(2).left();
		innerTable.row();
		innerTable.add(settingsState.createAllowSharedBokCheckbox(uiSkin)).colspan(2).left();
		innerTable.row();
		innerTable.add(settingsState.createTutorialModeCheckbox(uiSkin)).colspan(2).left();
		innerTable.row();
		innerTable.add(numberOfLivesLabel).left();
		innerTable.add(settingsState.createActualNumberOfLivesDisplayLabel(uiSkin)).left();
		innerTable.row();
		innerTable.add(settingsState.createNumberOfLivesSlider(uiSkin)).colspan(2).left().padBottom(5f);
		innerTable.row();

		table.add(innerTable).padBottom(5f);
		table.row();
		table.add(backButton);
		
		addActor(table);
	}
}
