package net.leejjon.bluffpoker.android.keyboard;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberCombinationInputFilter implements InputFilter {
    private final int mMax = 3;

    /**
     *
     * @param source The input that is trying to get added to the textfield.
     * @param start The start index of the input.
     * @param end The end index of the input.
     * @param dest The current value in the textfield.
     * @param dstart The start of the current value in the textfield.
     * @param dend The end of the current value in the textfield.
     * @return The overwritten value in case we filtered something out.
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int keep = mMax - (dest.length() - (dend - dstart));

        Pattern numberCombinationPattern = Pattern.compile("[0-6]");
        for (int i = 0; i < source.length(); i++) {
            Matcher matcher = numberCombinationPattern.matcher(new Character(source.charAt(i)).toString());
            if (!matcher.matches()) {
                return "";
            }
        }

        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return null; // keep original
        } else {
            keep += start;
            // No idea what this code does. But it works.
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            return source.subSequence(start, keep);
        }
    }
}
