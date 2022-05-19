package com.joebrooks.showmethecoin.presentation;

import com.joebrooks.showmethecoin.strategy.Strategy;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.repository.user.UserService;
import com.joebrooks.showmethecoin.repository.userConfig.UserConfigService;
import com.joebrooks.showmethecoin.trade.upbit.client.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/backtest")
public class BackTestController {

    private final UserConfigService userConfigService;
    private final UserService userService;

    @GetMapping
    public String showBackTestForm(Model model,
                                   HttpSession session){

        String userId = session.getAttribute("userId").toString();
        UserEntity userEntity = userService.getUser(userId).orElseThrow(() -> {
            throw new IllegalAccessError();
        });

        model.addAttribute("coinList", CoinType.values());
        model.addAttribute("userInfo", userConfigService.getUserConfig(userEntity).get());
        model.addAttribute("strategyLst", Strategy.values());


        return "backtest";
    }

}
