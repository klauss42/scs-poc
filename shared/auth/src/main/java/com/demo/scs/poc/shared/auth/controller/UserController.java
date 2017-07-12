package com.demo.scs.poc.shared.auth.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("authorizationRequest")
public class UserController {

    @RequestMapping("/user")
    @ResponseBody
    public Principal user(Principal user) {
        return user;
    }
}
