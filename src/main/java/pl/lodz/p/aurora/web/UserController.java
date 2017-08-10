package pl.lodz.p.aurora.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.lodz.p.aurora.service.UserService;

/**
 * REST controller for managing any requests related to user account.
 */
@Controller
public class UserController {

    @Autowired private UserService userService;
}
