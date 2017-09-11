package pl.lodz.p.aurora.configuration.security.jwt;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Marek on 9/10/2017.
 */
public class TokenConfigurationData {

    private final Integer tokenExpiresInHours = 168;
    private final String tokenHeader = "Authorization";
    private final String tokenPrefix = "Bearer ";
    @Value("${aurora.security.jwt.secret}")
    private String tokenSecretKey;

    public Integer getTokenExpiresInHours() {
        return tokenExpiresInHours;
    }

    public String getTokenHeader() {
        return tokenHeader;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public String getTokenSecretKey() {
        return tokenSecretKey;
    }
}
