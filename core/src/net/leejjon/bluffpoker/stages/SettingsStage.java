package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.leejjon.bluffpoker.logic.Settings;
import net.leejjon.bluffpoker.listener.ChangeStageListener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingsStage extends AbstractStage {
	private final CheckBox switchPositionsCheckBox;
	private final CheckBox allowBokCheckBox;
	private final CheckBox allowSharedBokCheckbox;
	private final Slider numberOfLivesSlider;
	
	public SettingsStage(Skin uiSkin, final ChangeStageListener changeScreen, Settings settings) {
		super(false);
		
		Label titleLabel = new Label("Settings", uiSkin);
		titleLabel.setColor(Color.WHITE);

		final Label numberOfLivesLabel = new Label("Number of lives: ", uiSkin);
		numberOfLivesLabel.setColor(Color.WHITE);

		final Label actualNumberOfLivesDisplayLabel = new Label("", uiSkin);
		actualNumberOfLivesDisplayLabel.setColor(Color.WHITE);

		switchPositionsCheckBox = new CheckBox("Allow switching positions", uiSkin);
		allowBokCheckBox = new CheckBox("Allow bok", uiSkin);
		allowSharedBokCheckbox = new CheckBox("Allow shared bok", uiSkin);
		numberOfLivesSlider = new Slider(1f, 10f, 1f, false, uiSkin);
		numberOfLivesSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				actualNumberOfLivesDisplayLabel.setText(new Float(numberOfLivesSlider.getValue()).intValue() + "");
			}
		});

		TextButton backButton = new TextButton("Back", uiSkin);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Settings newSettings = new Settings();
				newSettings.setAllowSwitchingPositions(switchPositionsCheckBox.isChecked());
				newSettings.setAllowBok(allowBokCheckBox.isChecked());
				newSettings.setAllowSharedBok(allowSharedBokCheckbox.isChecked());
				newSettings.setNumberOfLives(new Float(numberOfLivesSlider.getValue()).intValue());
				changeScreen.closeSettingsStage(newSettings);
			}
		});
		
		// Create a table and add the ui components to it.
		table.center();
		table.add(titleLabel);
		table.row();
		
		Table innerTable = new Table(uiSkin);
		innerTable.setFillParent(false);
		innerTable.row();
		innerTable.add(switchPositionsCheckBox).colspan(2).left();
		innerTable.row();
		innerTable.add(allowBokCheckBox).colspan(2).left();
		innerTable.row();
		innerTable.add(allowSharedBokCheckbox).colspan(2).left();
		innerTable.row();
		innerTable.add(numberOfLivesLabel).left();
		innerTable.add(actualNumberOfLivesDisplayLabel).left();
		innerTable.row();
		innerTable.add(numberOfLivesSlider).colspan(2).left().padBottom(5f);
		innerTable.row();

		table.add(innerTable).padBottom(5f);
		table.row();
		table.add(backButton);
		
		addActor(table);
	}
	
	public void loadLatestSettings(Settings settings) {
		switchPositionsCheckBox.setChecked(settings.isAllowSwitchingPositions());
		allowBokCheckBox.setChecked(settings.isAllowBok());
		allowSharedBokCheckbox.setChecked(settings.isAllowSharedBok());
		numberOfLivesSlider.setValue(settings.getNumberOfLives());
	}
}
