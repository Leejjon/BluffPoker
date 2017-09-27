package net.leejjon.bluffpoker.listener;

import com.badlogic.gdx.utils.Array;

public interface ModifyPlayerListener {
	void addContactsToGame(String ... playerNames);
	void loadFromPhonebook(Array<String> phoneBookContactNames);
	void showAndReset();
}
