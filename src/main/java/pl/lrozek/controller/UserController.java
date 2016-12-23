package pl.lrozek.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    public static final String USER_PATH = "/user";

    @RequestMapping(USER_PATH)
    public String getUserName( Principal principal ) {
        return principal.getName();
    }

}
