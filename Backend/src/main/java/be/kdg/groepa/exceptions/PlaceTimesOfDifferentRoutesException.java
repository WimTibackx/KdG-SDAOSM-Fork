package be.kdg.groepa.exceptions;

/**
 * Created by delltvgateway on 3/8/14.
 */
public class PlaceTimesOfDifferentRoutesException extends Exception {
    public PlaceTimesOfDifferentRoutesException() {
        super();
    }

    public PlaceTimesOfDifferentRoutesException(String message) {
        super(message);
    }

    public PlaceTimesOfDifferentRoutesException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlaceTimesOfDifferentRoutesException(Throwable cause) {
        super(cause);
    }

    protected PlaceTimesOfDifferentRoutesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
