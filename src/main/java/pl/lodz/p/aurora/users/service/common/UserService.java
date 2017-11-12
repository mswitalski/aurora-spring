package pl.lodz.p.aurora.users.service.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.users.domain.dto.UserSearchDto;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.List;

public interface UserService {

    void delete(Long userId, String eTag);

    List<User> findAll();

    Page<User> findAllByPage(Pageable pageable);

    User findById(Long userId);

    User findByUsername(String username);

    Page<User> search(UserSearchDto critieria, Pageable pageable);
}
