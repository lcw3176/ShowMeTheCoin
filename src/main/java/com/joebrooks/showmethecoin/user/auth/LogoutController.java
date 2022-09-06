package com.joebrooks.showmethecoin.user.auth;

import com.joebrooks.showmethecoin.auth.AuthManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/logout")
@RequiredArgsConstructor
public class LogoutController {

    private final AuthManager authManager;

    @GetMapping
    public String logout(HttpSession session){
        authManager.removeSession(session);

        return "redirect:/login";
    }
}
