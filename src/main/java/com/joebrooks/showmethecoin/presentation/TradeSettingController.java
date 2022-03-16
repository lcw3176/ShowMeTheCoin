package com.joebrooks.showmethecoin.presentation;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.repository.user.UserService;
import com.joebrooks.showmethecoin.upbit.client.CoinType;
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

    private final UserService userService;

    @GetMapping
    public String showTradeSetting(Model model,
                                   HttpSession session,
                                   @RequestParam(value = "result", defaultValue = "",required = false) String result){
        String userId = session.getAttribute("userId").toString();

        model.addAttribute("coinList", CoinType.values());
        model.addAttribute("userInfo", userService.getUser(userId).get());
        model.addAttribute("result", result);

        return "trade-setting";
    }

    @PostMapping
    public String saveUserSetting(@ModelAttribute TradeResponse body, HttpSession session){
        String userId = session.getAttribute("userId").toString();
        UserEntity user = userService.getUser(userId).orElseThrow(() ->{
            throw new IllegalAccessError();
        });

        user.changeTradeCoin(body.getTradeCoin());
        user.changeStartPrice(body.getStartPrice());

        userService.save(user);

        return "redirect:/trade-setting?result=success";
    }


    @Data
    public class TradeResponse {
        private CoinType tradeCoin;
        private double startPrice;
    }
}
