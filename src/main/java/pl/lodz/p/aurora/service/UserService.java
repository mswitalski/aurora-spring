package pl.lodz.p.aurora.service;

import pl.lodz.p.aurora.domain.entity.User;

import java.util.List;

/**
 * Interface for service for user feature.
 */
public interface UserService {

    List<User> findAll();
}
