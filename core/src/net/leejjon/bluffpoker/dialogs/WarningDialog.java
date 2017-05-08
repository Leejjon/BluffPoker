package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class WarningDialog extends Dialog {

    public WarningDialog(Skin skin) {
        super("Warning", skin);
        button("Ok");
    }

	public WarningDialog(String warningMessage, Skin skin) {
		super("Warning", skin);
		text(new Label(warningMessage, skin, "console", Color.BLACK));
        TextButton.TextButtonStyle textButtonStyle = skin.get(TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle otherTextButtonStyle = new TextButton.TextButtonStyle(textButtonStyle.up, textButtonStyle.down, textButtonStyle.checked, skin.getFont("console"));
        button(new TextButton("Ok", otherTextButtonStyle));
	}

	public void setRuntimeSpecificWarning(String runtimeSpecificWarning) {
        getContentTable().clearChildren();
        text(runtimeSpecificWarning);
	}
}
