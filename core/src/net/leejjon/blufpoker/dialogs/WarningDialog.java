package net.leejjon.blufpoker.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class WarningDialog extends Dialog {

	public WarningDialog(String warningMessage, Skin skin) {
		super("Warning", skin);
		text(warningMessage);
		button("Ok");
	}

}
