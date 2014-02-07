package be.kdg.groepa.exceptions;

/**
 * Created by Pieter-Jan on 6-2-14.
 */
public class UserExistException extends Exception {

    public UserExistException() {
    }

    public UserExistException(String message) {
        super(message);
    }
}
