package pl.lodz.p.aurora.common.service;

import pl.lodz.p.aurora.common.domain.entity.BaseEntity;
import pl.lodz.p.aurora.common.domain.entity.VersionedEntity;
import pl.lodz.p.aurora.common.exception.InvalidResourceRequestedException;
import pl.lodz.p.aurora.common.exception.OutdatedEntityModificationException;
import pl.lodz.p.aurora.common.util.EntityVersionTransformer;

/**
 * An abstract service that will serve as base for other services.
 */
public abstract class BaseService {

    private final EntityVersionTransformer versionTransformer;

    protected BaseService(EntityVersionTransformer versionTransformer) {
        this.versionTransformer = versionTransformer;
    }

    /**
     * Validate whether entity sent in update request is outdated or not. If yes then throw relevant exception.
     *
     * @param eTag Value of ETag header from the request
     * @param entity Relevant entity from data source
     */
    protected void failIfEncounteredOutdatedEntity(String eTag, VersionedEntity entity) {
        if (!isValid(eTag, entity.getVersion())) {
            throw new OutdatedEntityModificationException(entity);
        }
    }

    /**
     * Check whether given hash is valid for given version.
     *
     * @param givenHash Hash to check
     * @param checkedAgainstVersion Version value to check against
     * @return true if hash is valid, false otherwise
     */
    private boolean isValid(String givenHash, Long checkedAgainstVersion) {
        return givenHash.equals(versionTransformer.hash(checkedAgainstVersion));
    }

    /**
     * Check whether client requested a resource that exist in the data source.
     *
     * @param entity Record fetched from data source (or null if no relevant record was found)
     * @param identifier What identifier was used to get this entity
     */
    protected void failIfNoRecordInDatabaseFound(BaseEntity entity, Object identifier) {
        if (entity == null) {
            throw new InvalidResourceRequestedException(identifier);
        }
    }
}
