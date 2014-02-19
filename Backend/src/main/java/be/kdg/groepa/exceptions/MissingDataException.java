package be.kdg.groepa.exceptions;

/**
 * Created by delltvgateway on 2/17/14.
 */
public class MissingDataException extends Exception {
    public MissingDataException() {
        super();
    }

    public MissingDataException(String message) {
        super(message);
    }

    public MissingDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingDataException(Throwable cause) {
        super(cause);
    }

    protected MissingDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
