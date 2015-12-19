package net.leejjon.blufpoker.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.leejjon.blufpoker.NumberCombination;

/**
 * Created by Leejjon on 23-10-2015.
 */
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
