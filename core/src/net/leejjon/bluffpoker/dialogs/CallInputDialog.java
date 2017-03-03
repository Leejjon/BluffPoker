package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.Input.TextInputListener;
import net.leejjon.bluffpoker.interfaces.UserInterface;

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
