package com.joebrooks.showmethecoin.user.controller;

import com.joebrooks.showmethecoin.global.util.PageGenerator;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.repository.user.UserService;
import com.joebrooks.showmethecoin.user.model.TradeLogResponse;
import com.joebrooks.showmethecoin.repository.tradelog.TradeLogEntity;
import com.joebrooks.showmethecoin.repository.tradelog.TradeLogService;
import com.joebrooks.showmethecoin.repository.userconfig.UserConfigEntity;
import com.joebrooks.showmethecoin.repository.userconfig.UserConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.constraints.Positive;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TradeLogController {

    private final UserService userService;
    private final UserConfigService userConfigService;
    private final TradeLogService tradeLogService;

    @GetMapping("/trade-log")
    public String showTradeInfo(Model model,
                                @Positive @RequestParam(name="page", defaultValue = "1") int page,
                                @RequestParam(name="command", required = false, defaultValue = "") String command,
                                @SessionAttribute String userId) {

        UserEntity user = userService.getUser(userId);
        UserConfigEntity userConfig = userConfigService.getUserConfig(user);

        if(command.equals("start")){
            userConfigService.startTrading(userConfig);
        } else if(command.equals("stop")){
            userConfigService.stopTrading(userConfig);
        }


        List<TradeLogEntity> lst = tradeLogService.getTradeLogs(user, page).getContent();

        int startPage = PageGenerator.getStartPage(page);
        int lastPage = PageGenerator.getLastPage(page, tradeLogService.getTotalSize(user));

        model.addAttribute("posts", lst);

        model.addAttribute("pageResponse", TradeLogResponse.builder()
                .startPage(startPage)
                .lastPage(lastPage)
                .nowPage(page)
                .nextPage(PageGenerator.getNextPage(lastPage, tradeLogService.getTotalSize(user)))
                .previousPage(PageGenerator.getPreviousPage(startPage))
                .build());

        model.addAttribute("status", userConfig.isTrading());


        return "trade-log";
    }


}
