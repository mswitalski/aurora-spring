package pl.lodz.p.aurora.users.web.dto;

import pl.lodz.p.aurora.users.domain.entity.Role;

import java.util.HashSet;
import java.util.Set;

/**
 * DTO class for the claims in the JWT token.
 */
public class TokenClaimsDto {

    private boolean enabled = true;
    private Set<Role> roles = new HashSet<>();

    public TokenClaimsDto(boolean enabled, Set<Role> roles) {
        this.enabled = enabled;
        this.roles = roles;
    }

    public TokenClaimsDto() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
