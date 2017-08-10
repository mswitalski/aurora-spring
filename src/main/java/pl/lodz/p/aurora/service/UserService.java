package pl.lodz.p.aurora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.aurora.domain.repository.UserRepository;

/**
 * Service class used for processing user account data.
 */
@Service
public class UserService {

    @Autowired private UserRepository userRepository;
}
