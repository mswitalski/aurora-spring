package pl.lodz.p.aurora.mus.service.admin;

import pl.lodz.p.aurora.mus.domain.entity.User;

public interface UserAdminService {

    User create(User user);

    void delete(Long userId, String eTag);

    void update(Long userId, User user, String eTag);

    void updatePassword(Long userId, String newPassword, String eTag);
}
