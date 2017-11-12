package pl.lodz.p.aurora.users.service.admin;

import pl.lodz.p.aurora.users.domain.entity.User;

public interface UserAdminService {

    User create(User user);

    void update(String eTag, User user);

    void updatePassword(Long userId, String newPassword, String eTag);
}
