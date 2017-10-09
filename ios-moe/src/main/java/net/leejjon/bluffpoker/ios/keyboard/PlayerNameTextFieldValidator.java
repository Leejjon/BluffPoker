package net.leejjon.bluffpoker.ios.keyboard;

import apple.foundation.struct.NSRange;
import apple.uikit.UITextField;
import apple.uikit.protocol.UITextFieldDelegate;
import net.leejjon.bluffpoker.stages.SelectPlayersStage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerNameTextFieldValidator implements UITextFieldDelegate {

    @Override
    public boolean textFieldShouldChangeCharactersInRangeReplacementString(UITextField textField, NSRange range, String textAdded) {
        Pattern playerNamePattern = Pattern.compile("[a-zA-Z0-9_ ]");

        final String newCall = textField.text() + textAdded;
        if (newCall.length() > SelectPlayersStage.MAX_PLAYER_NAME_LENGTH) {
            return false;
        }

        for (int i = 0; i < textAdded.length(); i++) {
            Matcher matcher = playerNamePattern.matcher(new Character(textAdded.charAt(i)).toString());
            if (!matcher.matches()) {
                return false;
            }
        }

        return true;
    }
}
