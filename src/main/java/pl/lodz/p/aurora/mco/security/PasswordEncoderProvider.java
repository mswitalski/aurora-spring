package pl.lodz.p.aurora.mco.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Utility providing default encoder for user password.
 */
@Component
final public class PasswordEncoderProvider {

    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(11);
    }
}