package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.leejjon.bluffpoker.logic.NumberCombination;

public class CallNotThreeIdenticalNumbersDialog extends Dialog {
    public CallNotThreeIdenticalNumbersDialog(Skin skin) {
        super("Warning", skin);
        button("Ok");
    }

    public void setInvalidCallMessage(NumberCombination call) {
        getContentTable().clearChildren();
        text("Call must be identical and higher than " + call);
    }
}
