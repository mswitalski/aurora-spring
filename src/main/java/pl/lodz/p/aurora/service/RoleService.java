package pl.lodz.p.aurora.service;

import pl.lodz.p.aurora.domain.entity.Role;

import java.util.List;

/**
 * Interface for service for user roles feature.
 */
public interface RoleService {

    List<Role> findAll();
    Role findByName(String name);
}
