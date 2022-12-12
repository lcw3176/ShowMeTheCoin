package com.joebrooks.showmethecoin.user.controller;


import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.repository.user.UserService;
import com.joebrooks.showmethecoin.repository.useraccount.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class RevenueController {

    private final UserService userService;
    private final UserAccountService userAccountService;


    @GetMapping("/revenue")
    public String showDashboard(Model model, @SessionAttribute String userId){
        UserEntity user = userService.getUser(userId);


        model.addAttribute("balance", userAccountService.getBalance(user));


        return "revenue";
    }

}
