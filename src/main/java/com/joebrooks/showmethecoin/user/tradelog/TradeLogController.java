package com.joebrooks.showmethecoin.user.tradelog;

import com.joebrooks.showmethecoin.auth.AuthManager;
import com.joebrooks.showmethecoin.global.util.PageGenerator;
import com.joebrooks.showmethecoin.repository.tradelog.TradeLogEntity;
import com.joebrooks.showmethecoin.repository.tradelog.TradeLogService;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.repository.userconfig.UserConfigEntity;
import com.joebrooks.showmethecoin.repository.userconfig.UserConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Positive;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/trade-log")
public class TradeLogController {

    private final TradeLogService tradeLogService;
    private final AuthManager authManager;
    private final UserConfigService userConfigService;

    @GetMapping
    public String showTradeInfo(Model model,
                                @Positive @RequestParam(name="page", defaultValue = "1") int page,
                                @RequestParam(name="command", required = false, defaultValue = "") String command,
                                HttpSession session) {

        UserEntity user = authManager.extractUserId(session);
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
