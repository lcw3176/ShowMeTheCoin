package com.joebrooks.showmethecoin.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trade-setting")
public class TradeSettingController {

    @GetMapping
    public String showTradeSetting(){
        return "trade-setting";
    }
}
