package net.leejjon.bluffpoker.listener;

public interface ModifyPlayerListener {
	void addContactsToGame(String ... playerNames);
	void loadFromPhonebook(String ... phoneBookContactNames);
	void showPhonebookDialog();
}
