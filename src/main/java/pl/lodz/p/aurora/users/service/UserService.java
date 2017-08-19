package pl.lodz.p.aurora.users.service;

import pl.lodz.p.aurora.common.exception.UniqueConstraintViolationException;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.List;

/**
 * Interface for service for users feature.
 */
public interface UserService {

    User create(User user) throws UniqueConstraintViolationException;
    List<User> findAll();
    User findByUsername(String username);
}