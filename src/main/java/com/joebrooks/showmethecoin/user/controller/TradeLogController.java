package com.joebrooks.showmethecoin.user.controller;

import com.joebrooks.showmethecoin.repository.user.UserService;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class TradeLogController {

    private final UserService userService;

    @GetMapping("/trade-log")
    public String showTradeInfo(Model model,
                                @Positive @RequestParam(name="page", defaultValue = "1") int page,
                                @RequestParam(name="command", required = false, defaultValue = "") String command,
                                @SessionAttribute String userId) {


        return "trade-log";
    }


}
