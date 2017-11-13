package pl.lodz.p.aurora.users.service.unitleader;

import pl.lodz.p.aurora.users.domain.entity.User;

public interface UserUnitLeaderService {

    User create(User user);

    void delete(Long userId, String eTag);

    void update(Long userId, User user, String eTag);
}
