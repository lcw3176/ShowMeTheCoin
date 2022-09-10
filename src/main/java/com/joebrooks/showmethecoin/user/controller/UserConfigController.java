package com.joebrooks.showmethecoin.user.controller;

import com.joebrooks.showmethecoin.repository.candlestore.CandleMinute;
import com.joebrooks.showmethecoin.trade.strategy.StrategyType;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.repository.user.UserService;
import com.joebrooks.showmethecoin.repository.userconfig.UserConfigEntity;
import com.joebrooks.showmethecoin.user.model.UserConfigRequest;
import com.joebrooks.showmethecoin.repository.userconfig.UserConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UserConfigController {

    private final UserService userService;
    private final UserConfigService userConfigService;

    @GetMapping("/user-config")
    public String showUserSetting(Model model, @SessionAttribute String userId) {
        UserEntity userEntity = userService.getUser(userId);

        model.addAttribute("strategyLst", Arrays.stream(StrategyType.values())
                .filter(i -> !i.equals(StrategyType.BASE))
                .collect(Collectors.toList()));

        model.addAttribute("userInfo", userConfigService.getUserConfig(userEntity));
        model.addAttribute("candleMinuteList", CandleMinute.values());

        return "user-config";
    }

    @PostMapping("/user-config")
    @ResponseStatus(value = HttpStatus.OK)
    public void saveUserSetting(@RequestBody UserConfigRequest request, @SessionAttribute String userId) {
        UserEntity userEntity = userService.getUser(userId);

        userConfigService.changeConfig(userEntity, UserConfigEntity.builder()
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
