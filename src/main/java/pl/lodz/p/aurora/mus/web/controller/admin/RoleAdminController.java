package pl.lodz.p.aurora.mus.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.aurora.msh.web.controller.BaseController;
import pl.lodz.p.aurora.mus.domain.entity.Role;
import pl.lodz.p.aurora.mus.service.common.RoleService;

import java.util.List;

/**
 * REST controller for managing any requests related to user roles in the system.
 */
@RequestMapping(value = "api/v1/roles/", headers = "Requester-Role=ADMIN")
@RestController
public class RoleAdminController extends BaseController {

    private final RoleService service;

    @Autowired
    public RoleAdminController(RoleService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Role> findAll() {
        return service.findAll();
    }
}
