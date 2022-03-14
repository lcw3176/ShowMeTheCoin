package com.joebrooks.showmethecoin.presentation;

import com.joebrooks.showmethecoin.repository.user.UserService;
import com.joebrooks.showmethecoin.upbitTrade.upbit.CoinType;
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
    public String saveUserSetting(@ModelAttribute TradeResponse body){

        return "redirect:/trade-setting?result=success";
    }


    @Data
    public class TradeResponse {
        private String tradeCoin;
        private long divideCount;
        private double startPrice;
        private double lastPrice;
    }
}
