package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.Gdx;
import net.leejjon.bluffpoker.listener.ModifyPlayerListener;

import com.badlogic.gdx.Input.TextInputListener;

public class AddNewPlayerDialog implements TextInputListener {
	private ModifyPlayerListener listener;
	
	public AddNewPlayerDialog(ModifyPlayerListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void input(String playerName) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				listener.addContactsToGame(playerName);
			}
		});
	}

	@Override
	public void canceled() {
		// Do nothing.
	}
}
