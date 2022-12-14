package com.joebrooks.showmethecoin.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/user/login")
    public String showLoginForm() {

        return "login";
    }
}
