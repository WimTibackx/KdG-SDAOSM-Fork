package be.kdg.groepa.exceptions;

/**
 * Created by Tim on 6/02/14.
 */
public class UsernameFormatException extends Exception {
    public UsernameFormatException(){};

    public UsernameFormatException(String message) {
        super(message);
    }
}
