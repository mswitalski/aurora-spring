package pl.lodz.p.aurora.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.users.domain.entity.Role;
import pl.lodz.p.aurora.users.domain.repository.RoleRepository;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl extends BaseService implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Find all roles saved in data source.
     *
     * @return List of all roles saved in data source
     */
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    /**
     * Find role saved in data source by name.
     *
     * @return Role with given name
     */
    @Override
    public Role findByName(String name) {
        Role storedRole = roleRepository.findByName(name);
        failIfNoRecordInDatabaseFound(storedRole, name);

        return storedRole;
    }
}
