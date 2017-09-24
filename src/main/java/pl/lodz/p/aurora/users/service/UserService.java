package pl.lodz.p.aurora.users.service;

import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.List;

/**
 * Interface for service for users feature.
 */
public interface UserService {

    User createAsAdmin(User user);
    User createAsUnitLeader(User user);
    List<User> findAll();
    User findByUsername(String username);
    User updateOwnAccount(String eTag, User user);
    User updateAccount(String eTag, User user);
    User enable(Long id, String eTag);
    User disable(Long id, String eTag);
    User assignRole(Long userId, String roleName, String eTag);
    User updatePasswordAsAdmin(Long userId, String newPassword, String eTag);
    boolean updateOwnPassword(String username, String newPassword, String oldPassword, String eTag);
}
