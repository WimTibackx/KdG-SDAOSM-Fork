package be.kdg.groepa.exceptions;

/**
 * Created by Pieter-Jan on 6-2-14.
 */
public class PasswordFormatException extends Exception {

    public PasswordFormatException() {
    }

    public PasswordFormatException(String message) {
        super(message);
    }
}
