package com.joebrooks.showmethecoin.endpoint.controller;

import com.joebrooks.showmethecoin.exchange.ExchangeResponse;
import com.joebrooks.showmethecoin.exchange.ExchangeUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ExchangeUseCase exchangeUseCase;

    @GetMapping("/")
    public String redirectDashboard() {

        return "redirect:/home";
    }

    @GetMapping("/home")
    public String showDashboard(Model model) {
        List<ExchangeResponse> responses = exchangeUseCase.getDuplicatedCoinPrices();
        model.addAttribute("response", responses);

        return "home";
    }

}
