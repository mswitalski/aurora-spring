package pl.lodz.p.aurora.mus.service.common;

import pl.lodz.p.aurora.mus.domain.entity.Role;

import java.util.List;

/**
 * Interface for service for users roles feature.
 */
public interface RoleService {

    List<Role> findAll();

    Role findByName(String name);
}
