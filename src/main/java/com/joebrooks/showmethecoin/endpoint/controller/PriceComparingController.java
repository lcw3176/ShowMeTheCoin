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
public class PriceComparingController {

    private final ExchangeUseCase exchangeUseCase;

    @GetMapping("/")
    public String redirectDashboard() {

        return "redirect:/from-coinone";
    }

    @GetMapping("/from-coinone")
    public String showCoinOne(Model model) {
        List<ExchangeResponse> responses = exchangeUseCase.getPriceWhenBuyFromCoinOne();
        model.addAttribute("response", responses);

        return "from-coinone";
    }

    @GetMapping("/from-upbit")
    public String showUpBit(Model model) {
        List<ExchangeResponse> responses = exchangeUseCase.getPriceWhenBuyFromUpBit();
        model.addAttribute("response", responses);

        return "from-upbit";
    }

}
