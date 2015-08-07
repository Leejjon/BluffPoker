package net.leejjon.blufpoker.stages;

import net.leejjon.blufpoker.Settings;
import net.leejjon.blufpoker.listener.ChangeStageListener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingsStage extends AbstractStage {
	private final CheckBox switchPositionsCheckBox;
	private final CheckBox allowBokCheckBox;
	private final CheckBox allowSharedBokCheckbox;
	
	public SettingsStage(int divideScreenByThis, Skin uiSkin, final ChangeStageListener changeScreen, Settings settings) {
		super(divideScreenByThis, false);
		
		Label titleLabel = new Label("Settings", uiSkin);
		titleLabel.setColor(Color.WHITE);
		
		switchPositionsCheckBox = new CheckBox("Allow switching positions during game", uiSkin);
		allowBokCheckBox = new CheckBox("Allow bok", uiSkin);
		allowSharedBokCheckbox = new CheckBox("Allow shared bok", uiSkin);
		
		TextButton backButton = new TextButton("Back", uiSkin);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Settings newSettings = new Settings();
				newSettings.setAllowSwitchingPositions(switchPositionsCheckBox.isChecked());
				newSettings.setAllowBok(allowBokCheckBox.isChecked());
				newSettings.setAllowSharedBok(allowSharedBokCheckbox.isChecked());
				
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
		innerTable.add(switchPositionsCheckBox);
		innerTable.row();
		innerTable.add(allowBokCheckBox).left();
		innerTable.row();
		innerTable.add(allowSharedBokCheckbox).left();
		
		table.add(innerTable).padBottom(5f);
		table.row();
		table.add(backButton);
		
		addActor(table);
	}
	
	public void loadLatestSettings(Settings settings) {
		switchPositionsCheckBox.setChecked(settings.isAllowSwitchingPositions());
		allowBokCheckBox.setChecked(settings.isAllowBok());
		allowSharedBokCheckbox.setChecked(settings.isAllowSharedBok());
	}
}
