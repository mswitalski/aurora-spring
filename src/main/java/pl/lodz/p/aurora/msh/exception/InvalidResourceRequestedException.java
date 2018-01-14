package pl.lodz.p.aurora.msh.exception;

/**
 * Unchecked exception thrown in situation when client requested non-existing resource from data source.
 */
public class InvalidResourceRequestedException extends RuntimeException {

    /**
     * Default constructor for this class.
     *
     * @param identifier Key associated to entity by which user tried to fetch an entity
     */
    public InvalidResourceRequestedException(Object identifier) {
        super("Requested non-existing entity by identifier: " + identifier);
    }
}
