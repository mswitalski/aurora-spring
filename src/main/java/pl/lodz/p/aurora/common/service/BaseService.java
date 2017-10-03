package pl.lodz.p.aurora.common.service;

import pl.lodz.p.aurora.common.domain.entity.BaseEntity;
import pl.lodz.p.aurora.common.domain.entity.VersionedEntity;
import pl.lodz.p.aurora.common.exception.InvalidResourceRequestedException;
import pl.lodz.p.aurora.common.exception.OutdatedEntityModificationException;

/**
 * An abstract service that will serve as base for other services.
 */
public abstract class BaseService {

    /**
     * Validate whether entity sent in update request is outdated or not. If yes then throw relevant exception.
     *
     * @param eTag Value of ETag header from the request
     * @param entity Relevant entity from data source
     */
    protected void failIfEncounteredOutdatedEntity(String eTag, VersionedEntity entity) {
        if (!eTag.equals(Integer.toString(entity.hashCode()))) {
            throw new OutdatedEntityModificationException(entity);
        }
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
