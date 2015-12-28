package net.leejjon.blufpoker.listener;

import java.util.List;

import net.leejjon.blufpoker.Settings;

public interface ChangeStageListener {
	void startSelectingPlayersToPlayWith();
	void openSettingsStage();
	void closeSettingsStage(Settings newSettings);
	void backToStartStage();
	void startGame(List<String> players);
}
