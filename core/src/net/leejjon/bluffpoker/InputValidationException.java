package net.leejjon.bluffpoker;

import java.io.IOException;

/**
 * Created by Leejjon on 12-10-2015.
 */
public class InputValidationException extends IOException {
    private String message;

    public InputValidationException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
