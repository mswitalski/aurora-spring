package pl.lodz.p.aurora.configuration.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Marek on 9/11/2017.
 */
@Component
class JwtConfigurationData {

    public Integer tokenExpiresInHours = 168;
    public String tokenHeader = "Authorization";
    public String tokenPrefix = "Bearer ";
    @Value("${aurora.security.jwt.secret}")
    public String tokenSecretKey;

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
