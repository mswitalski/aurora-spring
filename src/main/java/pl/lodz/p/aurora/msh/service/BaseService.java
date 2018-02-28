package pl.lodz.p.aurora.msh.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.aurora.msh.domain.entity.VersionedEntity;
import pl.lodz.p.aurora.msh.exception.*;

import javax.validation.ConstraintViolationException;
import java.io.Serializable;
import java.util.regex.*;

/**
 * An abstract service that will serve as base for other services.
 */
public abstract class BaseService {

    /**
     * Validate whether entity sent in update request is outdated or not. If yes then throw relevant exception.
     *
     * @param eTag   Value of ETag header from the request
     * @param entity Relevant entity from data source
     */
    protected void failIfEncounteredOutdatedEntity(String eTag, VersionedEntity entity) {
        try {
            if (Long.parseLong(eTag) != entity.getVersion()) {
                throw new OutdatedEntityModificationException(entity);
            }

        } catch (NumberFormatException e) {
            throw new InvalidRequestException("Provided ETag value was not a number", e);
        }
    }

    /**
     * Check whether client requested a resource that exist in the data source.
     *
     * @param entity     Record fetched from data source (or null if no relevant record was found)
     * @param identifier What identifier was used to get this entity
     */
    protected void failIfNoRecordInDatabaseFound(Object entity, Object identifier) {
        if (entity == null) {
            throw new InvalidResourceRequestedException(identifier);
        }
    }

    /**
     * Save given entity to data source and return managed entity.
     *
     * @param entity Entity object that we want to save to data source
     * @param repository Repository for object that we want to save
     * @return Managed entity saved to data source
     * @throws UniqueConstraintViolationException when provided entity violates unique constraints
     * @throws InvalidEntityStateException when entity has invalid state in spite of previously DTO validation
     */
    protected <T, ID extends Serializable> T save(T entity, JpaRepository<T, ID> repository) {
        try {
            return repository.saveAndFlush(entity);

        } catch (DataIntegrityViolationException exception) {
            failOnUniqueConstraintViolation(exception, entity);

        } catch (ConstraintViolationException exception) {
            throw new InvalidEntityStateException(entity, exception);
        }

        return null;
    }

    /**
     * Fail on unique constraint violation and provide name of the constraint that was violated.
     *
     * @param exception Data integrity violation exception
     * @throws UniqueConstraintViolationException       when provided entity violates unique constraints
     * @throws InvalidApplicationConfigurationException when could not found the name of the field that violated unique
     *                                                  constraint
     */
    private void failOnUniqueConstraintViolation(DataIntegrityViolationException exception, Object entity)
            throws UniqueConstraintViolationException, InvalidApplicationConfigurationException {
        String message = exception.getCause().getCause().getMessage();
        Pattern pattern = Pattern.compile("unique_([a-zA-Z-]+)_([a-zA-Z-]+)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            throw new UniqueConstraintViolationException(exception, entity.getClass().getSimpleName(), matcher.group(2));
        }

        throw new InvalidApplicationConfigurationException(
                "Could not match any violated unique constraint in message",
                exception.getCause().getCause());
    }
}
