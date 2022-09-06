package com.joebrooks.showmethecoin.presentation.userconfig;


import com.joebrooks.showmethecoin.auth.AuthManager;
import com.joebrooks.showmethecoin.trade.strategy.StrategyType;
import com.joebrooks.showmethecoin.repository.candle.CandleMinute;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.repository.userconfig.UserConfigEntity;
import com.joebrooks.showmethecoin.repository.userconfig.UserConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user-config")
public class UserConfigController {

    private final UserConfigService userConfigService;
    private final AuthManager authManager;

    @GetMapping
    public String showUserSetting(Model model, HttpSession session) {
        UserEntity userEntity = authManager.extractUserId(session);

        model.addAttribute("strategyLst", Arrays.stream(StrategyType.values())
                .filter(i -> !i.equals(StrategyType.BASE))
                .collect(Collectors.toList()));

        model.addAttribute("userInfo", userConfigService.getUserConfig(userEntity));
        model.addAttribute("candleMinuteList", CandleMinute.values());

        return "user-config";
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public void saveUserSetting(@RequestBody UserConfigRequest request, HttpSession session) {
        UserEntity userId = authManager.extractUserId(session);
        userConfigService.changeConfig(userId, UserConfigEntity.builder()
                .strategy(request.getStrategyType())
                .maxBetCount(request.getMaxBetCount())
                .maxTradeCoinCount(request.getMaxTradeCoinCount())
                .candleMinute(request.getCandleMinute())
                .orderCancelMinute(request.getOrderCancelMinute())
                .allowSellWithLoss(request.isAllowSellWithLoss())
                .cashDividedCount(request.getCashDividedCount())
                .build());
    }



}