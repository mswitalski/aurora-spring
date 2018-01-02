package pl.lodz.p.aurora.configuration.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT token configuration data.
 */
@Component
class TokenConfigurationData {

    @Value("${aurora.security.jwt.token.expires_in_hours}")
    private Integer tokenExpiresInHours;
    @Value("${aurora.security.jwt.token.header}")
    private String tokenHeader;
    @Value("${aurora.security.jwt.token.prefix}")
    private String tokenPrefix;
    @Value("${aurora.security.jwt.secret}")
    private String tokenSecretKey;

    Integer getTokenExpiresInHours() {
        return tokenExpiresInHours;
    }

    String getTokenHeader() {
        return tokenHeader;
    }

    String getTokenPrefix() {
        return tokenPrefix;
    }

    String getTokenSecretKey() {
        return tokenSecretKey;
    }
}
