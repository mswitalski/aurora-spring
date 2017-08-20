package pl.lodz.p.aurora.users.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.aurora.users.domain.entity.Role;
import pl.lodz.p.aurora.users.service.RoleService;

import java.util.List;

/**
 * REST controller for managing any requests related to user roles in the system.
 */
@RequestMapping("api/roles/")
@RestController
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Role> findAll() {
        return roleService.findAll();
    }
}
