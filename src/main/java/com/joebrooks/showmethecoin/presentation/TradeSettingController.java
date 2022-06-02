package com.joebrooks.showmethecoin.presentation;

import com.joebrooks.showmethecoin.strategy.Strategy;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.repository.user.UserService;
import com.joebrooks.showmethecoin.repository.userConfig.UserConfigEntity;
import com.joebrooks.showmethecoin.repository.userConfig.UserConfigService;
import com.joebrooks.showmethecoin.trade.upbit.client.CoinType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/trade-setting")
public class TradeSettingController {

    private final UserConfigService userConfigService;
    private final UserService userService;

    @GetMapping
    public String showTradeSetting(Model model,
                                   HttpSession session,
                                   @RequestParam(value = "result", defaultValue = "",required = false) String result){
        String userId = session.getAttribute("userId").toString();
        UserEntity userEntity = userService.getUser(userId).orElseThrow(() -> {
            throw new IllegalAccessError();
        });

        model.addAttribute("coinList", CoinType.values());
        model.addAttribute("userInfo", userConfigService.getUserConfig(userEntity).get());
        model.addAttribute("strategyLst", Strategy.values());
        model.addAttribute("result", result);

        return "trade-setting";
    }

    @PostMapping
    public String saveUserSetting(@ModelAttribute TradeResponse body, HttpSession session){
        String userId = session.getAttribute("userId").toString();
        UserEntity userEntity = userService.getUser(userId).orElseThrow(() ->{
            throw new IllegalAccessError();
        });

        UserConfigEntity userConfigEntity = userConfigService.getUserConfig(userEntity).get();

        userConfigEntity.changeTradeCoin(body.getTradeCoin());
        userConfigService.save(userConfigEntity);

        return "redirect:/trade-setting?result=success";
    }


    @Data
    public class TradeResponse {
        private CoinType tradeCoin;
        private double startPrice;
        private double commonDifference;
        private Strategy strategy;
    }
}
