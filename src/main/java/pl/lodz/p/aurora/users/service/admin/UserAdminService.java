package pl.lodz.p.aurora.users.service.admin;

import pl.lodz.p.aurora.users.domain.entity.User;

public interface UserAdminService {

    User create(User user);

    void delete(Long userId, String eTag);

    void update(Long userId, User user, String eTag);

    void updatePassword(Long userId, String newPassword, String eTag);
}
