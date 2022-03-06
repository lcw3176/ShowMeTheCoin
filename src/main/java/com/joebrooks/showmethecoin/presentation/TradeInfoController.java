package com.joebrooks.showmethecoin.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trade-info")
public class TradeInfoController {

    @GetMapping
    public String showTradeInfo(){
        return "trade-info";
    }

}
