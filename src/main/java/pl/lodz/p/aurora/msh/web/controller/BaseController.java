package pl.lodz.p.aurora.msh.web.controller;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import pl.lodz.p.aurora.msh.domain.entity.VersionedEntity;

/**
 * An abstract controller that will serve as base for other controllers.
 */
public abstract class BaseController {

    /**
     * Clean received ETag value and leave only lowercase letters, uppercase letters and digits.
     *
     * @param eTag Received ETag value
     * @return Sanitized ETag
     */
    protected String sanitizeReceivedETag(String eTag) {
        return eTag.replaceAll("[^0-9]", "");
    }

    protected <T, E extends VersionedEntity> ResponseEntity<T> respondWithETag(E entity, Converter<E, T> converter) {
        return ResponseEntity.ok().eTag(Long.toString(entity.getVersion())).body(converter.convert(entity));
    }
}
