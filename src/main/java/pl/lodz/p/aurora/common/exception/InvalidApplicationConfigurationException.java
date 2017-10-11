package pl.lodz.p.aurora.common.exception;

public class InvalidApplicationConfigurationException extends RuntimeException {

    public InvalidApplicationConfigurationException(String message) {
        super(message);
    }

    public InvalidApplicationConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
