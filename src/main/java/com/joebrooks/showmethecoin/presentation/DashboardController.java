package com.joebrooks.showmethecoin.presentation;

import com.joebrooks.showmethecoin.repository.dailyScore.DailyScoreEntity;
import com.joebrooks.showmethecoin.repository.dailyScore.DailyScoreService;
import com.joebrooks.showmethecoin.repository.user.UserService;
import com.joebrooks.showmethecoin.repository.userConfig.UserConfigEntity;
import com.joebrooks.showmethecoin.repository.userConfig.UserConfigService;
import com.joebrooks.showmethecoin.upbit.account.AccountResponse;
import com.joebrooks.showmethecoin.upbit.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DailyScoreService dailyScoreService;
    private final UserService userService;
    private final UserConfigService userConfigService;
    private final AccountService accountService;

    @GetMapping("/")
    public String redirectDashboard(){

        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session){
        String id = (String)session.getAttribute("userId");

        userService.getUser(id).ifPresent((user) -> {
            List<DailyScoreEntity> lst = dailyScoreService.getScore(user);
            long revenue = 0L;

            for (DailyScoreEntity dailyScore : lst) {
                revenue += dailyScore.getTodayEarnPrice();
            }

            UserConfigEntity userConfigEntity = userConfigService.getUserConfig(user).orElse(
                    UserConfigEntity.builder()
                            .balance(0)
                            .build());

            model.addAttribute("balance",  (long)userConfigEntity.getBalance());
            model.addAttribute("revenue", revenue);
            model.addAttribute("daily", lst);
        });


        return "dashboard";
    }

    @PostMapping("/dashboard")
    public String refreshDashboard(HttpSession session){
        String id = (String)session.getAttribute("userId");

        userService.getUser(id).ifPresent((user) -> {
            AccountResponse accountResponse =accountService.getKRWCurrency();

            UserConfigEntity userConfigEntity = userConfigService.getUserConfig(user).orElse(
                    UserConfigEntity.builder()
                            .balance(0)
                            .build());

            userConfigEntity.changeBalance(Double.parseDouble(accountResponse.getBalance()));
            userConfigService.save(userConfigEntity);
        });

        return "redirect:/dashboard";
    }
}
