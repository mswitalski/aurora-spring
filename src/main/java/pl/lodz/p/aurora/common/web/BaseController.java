package pl.lodz.p.aurora.common.web;

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
        return eTag.replaceAll("[^0-9-]", "");
    }
}
