package pl.lodz.p.aurora.helper;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.users.domain.entity.Role;
import pl.lodz.p.aurora.users.domain.repository.RoleRepository;

/**
 * Utility class providing test data for features related to roles.
 */
@Component
public class RoleDataFactory extends EntityDataFactory<Role> {

    private RandomStringGenerator randomGenerator = new RandomStringGenerator.Builder()
            .withinRange('A', 'Z').build();

    @Autowired
    public void setRepository(RoleRepository roleRepository) {
        repository = roleRepository;
    }

    /**
     * Provide a single dummy role entity without saving to database.
     *
     * @return Dummy role entity saved to database
     */
    @Override
    public Role createSingle() {
        String randomString = randomGenerator.generate(10);

        return new Role(randomString);
    }
}
