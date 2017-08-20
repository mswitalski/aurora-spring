package pl.lodz.p.aurora.common.exception;

/**
 * Unchecked exception thrown in situation when entity has an invalid state. Also a many-to-many relationship with
 * other entity could be invalid because a null was intended to be inserted into data source.
 */
public class InvalidEntityStateException extends RuntimeException {

    public InvalidEntityStateException(Object invalidEntity, Throwable cause) {
        super("Processed entity has invalid state or tried to insert null in many-to-many relationship: "
                + invalidEntity.toString(), cause);
    }
}
