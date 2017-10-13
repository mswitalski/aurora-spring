package pl.lodz.p.aurora.users.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.aurora.users.service.DutyService;

/**
 * REST controller for managing any requests related to user duties in the system.
 */
@RequestMapping("api/")
@RestController
public class DutyController {

    private final DutyService dutyService;

    @Autowired
    public DutyController(DutyService dutyService) {
        this.dutyService = dutyService;
    }
}
