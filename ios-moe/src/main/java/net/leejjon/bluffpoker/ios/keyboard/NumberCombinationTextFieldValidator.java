package net.leejjon.bluffpoker.ios.keyboard;

import apple.foundation.struct.NSRange;
import apple.uikit.UITextField;
import apple.uikit.protocol.UITextFieldDelegate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberCombinationTextFieldValidator implements UITextFieldDelegate {
    private static final int MAX_LENGTH = 3;

    @Override
    public boolean textFieldShouldChangeCharactersInRangeReplacementString(UITextField textField, NSRange range, String textAdded) {
        Pattern numberCombinationPattern = Pattern.compile("[0-6]");

        final String newCall = textField.text() + textAdded;
        if (newCall.length() > MAX_LENGTH) {
            return false;
        }

        for (int i = 0; i < textAdded.length(); i++) {
            Matcher matcher = numberCombinationPattern.matcher(new Character(textAdded.charAt(i)).toString());
            if (!matcher.matches()) {
                return false;
            }
        }

        return true;
    }
}
