package net.leejjon.bluffpoker.dialogs;

import net.leejjon.bluffpoker.listener.ModifyPlayerListener;

import com.badlogic.gdx.Input.TextInputListener;

public class AddNewPlayerDialog implements TextInputListener {
	private ModifyPlayerListener listener;
	
	public AddNewPlayerDialog(ModifyPlayerListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void input(String playerName) {
		listener.addContactsToGame(playerName);
	}

	@Override
	public void canceled() {
		// Do nothing.
	}
}
