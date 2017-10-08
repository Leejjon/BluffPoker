package net.leejjon.bluffpoker.android.keyboard;

import android.text.InputFilter;
import android.text.Spanned;
import net.leejjon.bluffpoker.stages.SelectPlayersStage;

public class PlayerNameInputFilter implements InputFilter {
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
        int keep = SelectPlayersStage.MAX_PLAYER_NAME_LENGTH - (dest.length() - (dend - dstart));

        boolean containsInvalidCharacters = false;

        if (source.toString().matches("[^\\p{ASCII}]")) {
            containsInvalidCharacters = true;
            source = source.toString().replaceAll("[^\\p{ASCII}]", "");
        }

        if (source.toString().matches("^[^a-zA-Z0-9_ ]{1,16}$")) {
            containsInvalidCharacters = true;
            source = source.toString().replaceAll("[^a-zA-Z0-9_ ]", "");
        }

        if (keep <= 0) {
            return "";
        } else if (keep >= end - start && !containsInvalidCharacters) {
            return null; // keep original
        } else if (containsInvalidCharacters) {
            return source; // replace fully
        } else {
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            return source.subSequence(start, keep); // replace only the part that fits.
        }
    }
}
