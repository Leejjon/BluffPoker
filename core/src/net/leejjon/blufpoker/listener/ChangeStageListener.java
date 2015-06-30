package net.leejjon.blufpoker.listener;

import net.leejjon.blufpoker.Settings;

public interface ChangeStageListener {
	void startGame();
	void openSettingsStage();
	void closeSettingsStage(Settings newSettings);
}
