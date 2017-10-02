package pl.lodz.p.aurora.users.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.users.domain.entity.User;

/**
 * Interface for service for users feature.
 */
public interface UserService {

    User createAsAdmin(User user);
    User createAsUnitLeader(User user);
    Page<User> findAllByPage(Pageable pageable);
    User findByUsername(String username);
    User updateOwnAccount(String eTag, User user);
    User updateAccount(String eTag, User user);
    User enable(Long id, String eTag);
    User disable(Long id, String eTag);
    User assignRole(Long userId, String roleName, String eTag);
    User updatePasswordAsAdmin(String username, String newPassword, String eTag);
    boolean updateOwnPassword(String username, String newPassword, String oldPassword, String eTag);
}
