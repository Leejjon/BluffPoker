package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;


public class CallInputDialog implements Input.TextInputListener {
    public final static String ENTER_THREE_DIGITS = "Enter your call:";

    private TextField callInputField;

    public CallInputDialog(TextField callInputField) {
        this.callInputField = callInputField;
    }

    @Override
    public void input(String text) {
        callInputField.setText(text);
    }

    @Override
    public void canceled() {
        // Do nothing.
    }
}
