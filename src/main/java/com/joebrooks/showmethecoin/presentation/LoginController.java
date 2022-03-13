package com.joebrooks.showmethecoin.presentation;

import com.joebrooks.showmethecoin.global.routine.mail.ReportSender;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.repository.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

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
                            HttpSession session) throws NoSuchAlgorithmException {

        Optional<UserEntity> user = userService.login(id, pw);
        if(user.isPresent()){
            session.setAttribute("userId", user.get().getUserId());

            return "redirect:/dashboard";
        }

        return "login";
    }
}
