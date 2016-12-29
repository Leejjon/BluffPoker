package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.leejjon.bluffpoker.logic.NumberCombination;

/**
 * Created by Leejjon on 19-12-2015.
 */
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
