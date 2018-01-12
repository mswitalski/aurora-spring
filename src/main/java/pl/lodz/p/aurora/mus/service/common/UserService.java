package pl.lodz.p.aurora.mus.service.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.mus.web.dto.UserSearchDto;
import pl.lodz.p.aurora.mus.domain.entity.User;

public interface UserService {

    Page<User> findAllByPage(Pageable pageable);

    User findById(Long userId);

    User findByUsername(String username);

    Page<User> search(UserSearchDto critieria, Pageable pageable);
}
