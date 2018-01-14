package pl.lodz.p.aurora.msh.exception;

/**
 * Unchecked exception thrown in situation when entity has an invalid state. Also a many-to-many relationship with
 * other entity could be invalid because a null was intended to be inserted into data source.
 */
public class InvalidEntityStateException extends RuntimeException {

    /**
     * Default constructor for this exception.
     *
     * @param invalidEntity Entity that has invalid state
     * @param cause What exactly caused this situation
     */
    public InvalidEntityStateException(Object invalidEntity, Throwable cause) {
        super("Processed entity has invalid state or tried to violate some constraints: "
                + invalidEntity.toString(), cause);
    }
}
