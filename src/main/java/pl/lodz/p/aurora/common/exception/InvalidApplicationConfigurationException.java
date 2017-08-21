package pl.lodz.p.aurora.common.exception;

/**
 * Unchecked exception thrown in situation when the application has invalid configuration and is not able
 * to take any further action.
 */
public class InvalidApplicationConfigurationException extends RuntimeException {

    public InvalidApplicationConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidApplicationConfigurationException(Throwable cause) {
        super(cause);
    }
}
