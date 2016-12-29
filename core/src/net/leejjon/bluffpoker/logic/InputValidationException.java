package net.leejjon.bluffpoker.logic;

import java.io.IOException;

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
