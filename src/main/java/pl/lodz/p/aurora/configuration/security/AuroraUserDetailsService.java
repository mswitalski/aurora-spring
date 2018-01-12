package pl.lodz.p.aurora.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.*;
import pl.lodz.p.aurora.mus.domain.entity.User;
import pl.lodz.p.aurora.mus.domain.repository.UserRepository;

/**
 * Service related to security that provides user account by given username.
 */
@Component
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true, transactionManager = "musTransactionManager")
public class AuroraUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public AuroraUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findDistinctByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }
}
