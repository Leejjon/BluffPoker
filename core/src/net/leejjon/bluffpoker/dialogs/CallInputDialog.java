package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import net.leejjon.bluffpoker.listener.UserInterface;
import net.leejjon.bluffpoker.stages.GameInputInterface;

public class CallInputDialog implements TextInputListener {
    public final static String ENTER_YOUR_CALL = "Enter your call:";
    public final static String ENTER_THREE_DIGITS = "Enter three numbers.";

    private UserInterface ui;

    public CallInputDialog(UserInterface ui) {
        this.ui = ui;
    }

    @Override
    public void input(String text) {
        ui.setCallField(text);
        ui.call();
    }

    @Override
    public void canceled() {
        // Do nothing.
    }
}
