package com.joebrooks.showmethecoin.presentation;

import com.joebrooks.showmethecoin.repository.trade.TradeEntity;
import com.joebrooks.showmethecoin.repository.trade.TradeService;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.repository.user.UserService;
import com.joebrooks.showmethecoin.repository.userConfig.UserConfigEntity;
import com.joebrooks.showmethecoin.repository.userConfig.UserConfigService;
import lombok.Getter;
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
    private final UserConfigService userConfigService;

    @GetMapping
    public String showTradeInfo(@RequestParam(value = "command", defaultValue = "", required = false) String command,
                                @RequestParam(value = "next", defaultValue = "", required = false) String next,
                                Model model,
                                HttpSession session) {

        String userId = (String)session.getAttribute("userId");
        
        // fixme 페이지네이션 정리좀
        if(session.getAttribute("userPage") == null){
            session.setAttribute("userPage", 0);
        }


        UserEntity user = userService.getUser(userId).orElseThrow(IllegalAccessError::new);
        UserConfigEntity userConfig = userConfigService.getUser(user).get();
        int page = Integer.parseInt(session.getAttribute("userPage").toString());

        if(next.equals("true")){
            page += 1;
        } else if(next.equals("false")){
            page -= 1;
        }

        if(page < 0){
            page = 0;
        }



        List<TradeEntity> lst = tradeService.getTradeLogs(user, page).getContent();

        if(lst.size() < 10){
            page -= 1;
        }

        session.setAttribute("userPage", page);

        if(command.equals(AutoCommand.RUN.getValue())){
            userConfig.changeTradeStatus(true);

            userConfigService.save(userConfig);
        } else if(command.equals(AutoCommand.STOP.getValue())){
            userConfig.changeTradeStatus(false);

            userConfigService.save(userConfig);
        }

        model.addAttribute("info", lst);
        model.addAttribute("status", userConfig.isTrading());
        return "trade-info";
    }

    @Getter
    private enum AutoCommand {
        RUN("run"),
        STOP("stop");

        String value;

        private AutoCommand(String value){
            this.value = value;
        }

    }

}
