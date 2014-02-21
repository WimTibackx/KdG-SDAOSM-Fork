package be.kdg.groepa.exceptions;

/**
 * Created by delltvgateway on 2/21/14.
 */
public class CarNotOfUserException extends Exception {
    public CarNotOfUserException() {
        super();
    }

    public CarNotOfUserException(String message) {
        super(message);
    }

    public CarNotOfUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public CarNotOfUserException(Throwable cause) {
        super(cause);
    }

    protected CarNotOfUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
