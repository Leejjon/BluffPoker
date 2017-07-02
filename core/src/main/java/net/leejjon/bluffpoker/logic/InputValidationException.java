package net.leejjon.bluffpoker.logic;

public class InputValidationException extends Exception {
    private String message;

    public InputValidationException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
