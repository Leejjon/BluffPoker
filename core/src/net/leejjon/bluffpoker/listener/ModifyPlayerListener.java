package net.leejjon.bluffpoker.listener;

public interface ModifyPlayerListener {
	void addContactsToGame(String ... playerNames);
	void selectFromPhoneBook(String ... phoneBookContactNames);
}
