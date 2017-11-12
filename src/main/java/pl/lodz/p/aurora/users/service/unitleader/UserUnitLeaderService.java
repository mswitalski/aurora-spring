package pl.lodz.p.aurora.users.service.unitleader;

import pl.lodz.p.aurora.users.domain.entity.User;

public interface UserUnitLeaderService {

    User create(User user);

    void update(String eTag, User user);
}
