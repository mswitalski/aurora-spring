package pl.lodz.p.aurora.users.service.common;

import pl.lodz.p.aurora.users.domain.entity.User;

public interface ProfileService {

    void update(String eTag, User user);

    boolean updatePassword(String username, String newPassword, String oldPassword, String eTag);
}
