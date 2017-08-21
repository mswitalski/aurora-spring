package pl.lodz.p.aurora.common.util;

import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class that is responsible for hashing and comparing entity version in order to allow handling
 * optimistic lock.
 */
@Component
public class EntityVersionTransformer {

    @Value("${aurora.entity.version.hash.salt}")
    private String hashSalt;

    /**
     * Hash given version with salt.
     *
     * @param version Value of version field from chosen entity
     * @return Salted hash of given version
     */
    public String hash(Long version) {
        String valueToHash = hashSalt + version.toString();

        return Hashing.sha512().hashBytes(valueToHash.getBytes()).toString();
    }
}
