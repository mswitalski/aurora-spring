package pl.lodz.p.aurora.mus.service.unitleader;

import pl.lodz.p.aurora.mus.domain.entity.User;

import java.util.List;

public interface UserUnitLeaderService {

    User create(User user);

    void delete(Long userId, String eTag);

    List<User> findAll();

    void update(Long userId, User user, String eTag);

    void updateDuties(Long userId, User user, String eTag);
}
