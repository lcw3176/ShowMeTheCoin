package com.joebrooks.showmethecoin.presentation.backtest;

import com.joebrooks.showmethecoin.autotrade.strategy.StrategyType;
import com.joebrooks.showmethecoin.autotrade.upbit.CoinType;
import com.joebrooks.showmethecoin.repository.candle.CandleMinute;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/backtest")
public class BackTestController {

    @GetMapping
    public String showBackTestForm(Model model){

        model.addAttribute("coinList", CoinType.values());
        model.addAttribute("strategyList", Arrays.stream(StrategyType.values())
                .filter(i -> !i.equals(StrategyType.BASE))
                .collect(Collectors.toList()));
        model.addAttribute("candleMinuteList", CandleMinute.values());

        return "backtest";
    }

}
