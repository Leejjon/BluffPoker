package net.leejjon.bluffpoker.listener;

import java.util.List;

import net.leejjon.bluffpoker.logic.Settings;

public interface ChangeStageListener {
	void startSelectingPlayersToPlayWith();
	void openSettingsStage();
	void closeSettingsStage(Settings newSettings);
	void backToStartStage();
	void startGame(List<String> players);
}
