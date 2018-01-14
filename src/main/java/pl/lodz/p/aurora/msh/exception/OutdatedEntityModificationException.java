package pl.lodz.p.aurora.msh.exception;

import pl.lodz.p.aurora.msh.domain.entity.VersionedEntity;

/**
 * Unchecked exception thrown in situation when the client tries to update entity in data source with outdated
 * data (different values of version field).
 */
public class OutdatedEntityModificationException extends RuntimeException {

    /**
     * Default constructor for this class.
     *
     * @param storedEntity Entity with data stored in data source
     */
    public OutdatedEntityModificationException(VersionedEntity storedEntity) {
        super("Client tried to update entity " + storedEntity + " with outdated data");
    }
}
