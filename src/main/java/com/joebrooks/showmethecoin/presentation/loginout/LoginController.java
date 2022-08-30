package com.joebrooks.showmethecoin.presentation.loginout;


import com.joebrooks.showmethecoin.auth.AuthManager;
import com.joebrooks.showmethecoin.repository.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;

    @GetMapping
    public String showLoginForm() {

        return "login";
    }

    @PostMapping
    public String checkUser(@RequestParam("id") String id,
                            @RequestParam("pw") String pw,
                            HttpSession session) {

        userService.login(id, pw);
        AuthManager.saveSession(session, id);

        return "redirect:/dashboard";
    }
}
