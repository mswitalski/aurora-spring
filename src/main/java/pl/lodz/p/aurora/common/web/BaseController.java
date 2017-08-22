package pl.lodz.p.aurora.common.web;

import org.springframework.http.HttpHeaders;
import pl.lodz.p.aurora.common.domain.entity.VersionedEntity;
import pl.lodz.p.aurora.common.util.EntityVersionTransformer;

/**
 * An abstract controller that will serve as base for other controllers.
 */
public abstract class BaseController {

    private final EntityVersionTransformer versionTransformer;

    public BaseController(EntityVersionTransformer versionTransformer) {
        this.versionTransformer = versionTransformer;
    }

    /**
     * Prepare HTTP headers containing an ETag header with hashed value of entity's version.
     *
     * @param entity Entity that version will be send in header in hashed form
     * @return Prepared HTTP header with an ETag header
     */
    protected HttpHeaders prepareETagHeaders(VersionedEntity entity) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("ETag", versionTransformer.hash(entity.getVersion()));

        return httpHeaders;
    }

    /**
     * Clean received ETag value and leave only lowercase letters, uppercase letters and digits.
     *
     * @param eTag Received ETag value
     * @return Sanitized ETag
     */
    protected String sanitizeReceivedETag(String eTag) {
        return eTag.replaceAll("[^a-zA-Z0-9]", "");
    }
}
