package pl.lodz.p.aurora.common.exception;

/**
 * Unchecked exception thrown in situation when entity has an invalid state. Normally this situation should not
 * be possible because user input in form of DTO object is previously validated. That means the system changed
 * somewhere in the way entity's state from valid to invalid.
 */
public class InvalidEntityStateException extends RuntimeException {

    public InvalidEntityStateException(Object invalidEntity, Throwable cause) {
        super("Processed entity has invalid state: " + invalidEntity.toString(), cause);
    }
}
