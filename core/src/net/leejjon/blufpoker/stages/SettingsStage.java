package net.leejjon.blufpoker.stages;

import net.leejjon.blufpoker.listener.ChangeStageListener;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SettingsStage extends AbstractStage {
	public SettingsStage(float w, float h, int divideScreenByThis, Skin uiSkin, final ChangeStageListener changeScreen) {
		super(w, h, divideScreenByThis, false);
	}
}
