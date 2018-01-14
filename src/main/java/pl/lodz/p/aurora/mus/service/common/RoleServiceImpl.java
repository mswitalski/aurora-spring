package pl.lodz.p.aurora.mus.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.msh.service.BaseService;
import pl.lodz.p.aurora.mus.domain.entity.Role;
import pl.lodz.p.aurora.mus.domain.repository.RoleRepository;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true, transactionManager = "musTransactionManager")
public class RoleServiceImpl extends BaseService implements RoleService {

    private final RoleRepository repository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    /**
     * Find all roles saved in data source.
     *
     * @return List of all roles saved in data source
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public List<Role> findAll() {
        return repository.findAll();
    }

    /**
     * Find role saved in data source by name.
     *
     * @return Role with given name
     */
    @PreAuthorize("hasRole('ROLE_UNIT_LEADER')")
    @Override
    public Role findByName(String name) {
        Role storedRole = repository.findByName(name);

        failIfNoRecordInDatabaseFound(storedRole, name);

        return storedRole;
    }
}
