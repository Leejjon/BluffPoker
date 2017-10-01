package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.leejjon.bluffpoker.state.GameState;
import net.leejjon.bluffpoker.state.Settings;
import net.leejjon.bluffpoker.interfaces.StageInterface;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingsStage extends AbstractStage {
	private final CheckBox allowBokCheckBox;
	private final CheckBox allowSharedBokCheckbox;
	private final CheckBox tutorialModeCheckbox;
	private final Slider numberOfLivesSlider;
	private final Label actualNumberOfLivesDisplayLabel;

	private Settings settings;
	
	public SettingsStage(Skin uiSkin, final StageInterface stageInterface, Settings settings) {
		super(false);
		this.settings = settings;

		Label titleLabel = new Label("Settings", uiSkin);
		titleLabel.setColor(Color.WHITE);

		Label numberOfLivesLabel = new Label("Number of lives: ", uiSkin);
		numberOfLivesLabel.setColor(Color.WHITE);

		actualNumberOfLivesDisplayLabel = new Label("", uiSkin);
		actualNumberOfLivesDisplayLabel.setColor(Color.WHITE);

		allowBokCheckBox = new CheckBox("Allow bok", uiSkin);
		allowSharedBokCheckbox = new CheckBox("Allow shared bok", uiSkin);
		tutorialModeCheckbox = new CheckBox("Tutorial mode", uiSkin);
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
				settings.setAllowBok(allowBokCheckBox.isChecked());
				settings.setAllowSharedBok(allowSharedBokCheckbox.isChecked());
				settings.setTutorialMode(tutorialModeCheckbox.isChecked());
				settings.setNumberOfLives(new Float(numberOfLivesSlider.getValue()).intValue());
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
		innerTable.add(allowBokCheckBox).colspan(2).left();
		innerTable.row();
		innerTable.add(allowSharedBokCheckbox).colspan(2).left();
		innerTable.row();
		innerTable.add(tutorialModeCheckbox).colspan(2).left();
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
	
	public void loadLatestSettings() {
		allowBokCheckBox.setChecked(settings.isAllowBok());
		allowSharedBokCheckbox.setChecked(settings.isAllowSharedBok());
		tutorialModeCheckbox.setChecked(settings.isTutorialMode());
		numberOfLivesSlider.setValue(settings.getNumberOfLives());
        actualNumberOfLivesDisplayLabel.setText(new Float(numberOfLivesSlider.getValue()).intValue() + "");
	}
}
