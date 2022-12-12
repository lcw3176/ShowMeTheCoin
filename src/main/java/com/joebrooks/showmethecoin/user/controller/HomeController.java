package com.joebrooks.showmethecoin.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String redirectDashboard(){

        return "redirect:/home";
    }

    @GetMapping("/home")
    public String showDashboard(Model model){

        return "home";
    }

}
