package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class WarningDialog extends Dialog {

    public WarningDialog(Skin skin) {
        super("Warning", skin);
    }

	public WarningDialog(String warningMessage, Skin skin) {
		super("Warning", skin);
		text(warningMessage);
		button("Ok");
	}

	public void setRuntimeSpecificWarning(String runtimeSpecificWarning) {
        getContentTable().clearChildren();
        text(runtimeSpecificWarning);
		button("Ok");
	}
}
