package com.joebrooks.showmethecoin.presentation;

import com.joebrooks.showmethecoin.repository.trade.TradeEntity;
import com.joebrooks.showmethecoin.repository.trade.TradeService;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.repository.user.UserService;
import com.joebrooks.showmethecoin.upbitTrade.auto.AutoCommand;
import com.joebrooks.showmethecoin.upbitTrade.auto.AutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/trade-info")
public class TradeInfoController {

    private final TradeService tradeService;
    private final UserService userService;
    private final AutoService autoService;

    @GetMapping
    public String showTradeInfo(@RequestParam(value = "command", defaultValue = "", required = false) String command,
                                Model model,
                                HttpSession session) {

        String userId = (String)session.getAttribute("userId");
        UserEntity user = userService.getUser(userId).orElseThrow(IllegalAccessError::new);

        List<TradeEntity> lst = tradeService.getTradeLogs(user, 0).getContent();

        if(command.equals(AutoCommand.RUN.toString().toLowerCase())){
            autoService.execute(AutoCommand.RUN);
            user.changeTradeStatus(true);

            userService.save(user);
        } else if(command.equals(AutoCommand.STOP.toString().toLowerCase())){
            autoService.execute(AutoCommand.STOP);
            user.changeTradeStatus(false);

            userService.save(user);
        }

        model.addAttribute("info", lst);
        model.addAttribute("status", user.isTrading());
        return "trade-info";
    }

}
