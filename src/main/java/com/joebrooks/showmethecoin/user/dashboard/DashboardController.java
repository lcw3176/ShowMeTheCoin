package com.joebrooks.showmethecoin.user.dashboard;

import com.joebrooks.showmethecoin.auth.AuthManager;
import com.joebrooks.showmethecoin.user.account.UserAccountService;
import com.joebrooks.showmethecoin.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DailyScoreService dailyScoreService;
    private final UserAccountService userAccountService;
    private final AuthManager authManager;

    @GetMapping("/")
    public String redirectDashboard(){

        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session){
        UserEntity user = authManager.extractUserId(session);
        List<DailyScoreEntity> dailyScoreList = dailyScoreService.getScore(user);

        model.addAttribute("balance", userAccountService.getBalance(user));
        model.addAttribute("revenue", dailyScoreList.stream()
                .mapToDouble(DailyScoreEntity::getTodayEarnPrice)
                .sum());
        model.addAttribute("daily", dailyScoreList);


        return "dashboard";
    }

}
