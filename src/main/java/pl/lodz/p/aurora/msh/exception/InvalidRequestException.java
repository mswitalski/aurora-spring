package pl.lodz.p.aurora.msh.exception;

public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
