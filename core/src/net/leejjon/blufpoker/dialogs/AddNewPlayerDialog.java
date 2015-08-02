package net.leejjon.blufpoker.dialogs;

import net.leejjon.blufpoker.listener.ModifyPlayerListener;

import com.badlogic.gdx.Input.TextInputListener;

public class AddNewPlayerDialog implements TextInputListener {
	private ModifyPlayerListener listener;
	private final int maxNameLength = 16;
	
	public AddNewPlayerDialog(ModifyPlayerListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void input(String playerName) {
		if (playerName.length() > 0 && playerName.length() <= maxNameLength) {
			listener.addNewPlayer(playerName);
		}
	}

	@Override
	public void canceled() {
		// Do nothing.
	}
}
