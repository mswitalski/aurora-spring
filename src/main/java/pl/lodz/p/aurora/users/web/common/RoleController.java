package pl.lodz.p.aurora.users.web.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.aurora.users.domain.entity.Role;
import pl.lodz.p.aurora.users.service.common.RoleService;

import java.util.List;

/**
 * REST controller for managing any requests related to user roles in the system.
 */
@RequestMapping(value = "api/v1/roles/", headers = "Requester-Role=ALL")
@RestController
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping()
    public List<Role> findAll() {
        return roleService.findAll();
    }
}
