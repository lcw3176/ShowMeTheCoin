package com.joebrooks.showmethecoin.endpoint.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/user/login")
    public String showLoginForm() {

        return "login";
    }
}
