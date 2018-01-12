package pl.lodz.p.aurora.mus.service.common;

import pl.lodz.p.aurora.mus.domain.entity.User;

public interface ProfileService {

    void update(String eTag, User user);

    boolean updatePassword(String username, String newPassword, String oldPassword, String eTag);
}
