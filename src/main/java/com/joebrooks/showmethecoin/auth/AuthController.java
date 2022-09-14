package com.joebrooks.showmethecoin.auth;

import com.joebrooks.showmethecoin.repository.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class AuthController {


    private final UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {

        return "login";
    }

    @PostMapping("/login")
    public String checkUser(@RequestParam("id") String id,
                            @RequestParam("pw") String pw,
                            HttpSession session) {

        userService.login(id, pw);
        session.setAttribute("userId", id);

        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("userId");

        return "redirect:/home";
    }

}
