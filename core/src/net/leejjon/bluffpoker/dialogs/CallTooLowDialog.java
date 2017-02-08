package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.leejjon.bluffpoker.logic.NumberCombination;

public class CallTooLowDialog extends Dialog {
    public CallTooLowDialog(Skin skin) {
        super("Warning", skin);
        button("Ok");
    }

    public void callTooLow(NumberCombination call) {
        getContentTable().clearChildren();
        text("Call must be higher than " + call);
    }
}
