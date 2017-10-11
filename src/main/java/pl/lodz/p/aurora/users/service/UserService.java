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
    void updateOwnAccount(String eTag, User user);
    void updateOtherAccount(String eTag, User user);
    void updatePasswordAsAdmin(Long userId, String newPassword, String eTag);
    boolean updateOwnPassword(String username, String newPassword, String oldPassword, String eTag);
    void delete(Long userId, String eTag);
}
