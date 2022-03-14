package com.joebrooks.showmethecoin.presentation;

import com.joebrooks.showmethecoin.repository.dailyScore.DailyScoreEntity;
import com.joebrooks.showmethecoin.repository.dailyScore.DailyScoreRepository;
import com.joebrooks.showmethecoin.repository.dailyScore.DailyScoreService;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.repository.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DailyScoreService dailyScoreService;
    private final UserService userService;

    @GetMapping("/")
    public String redirectDashboard(){

        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session){
        String id = (String)session.getAttribute("userId");
        long revenue = 0L;

        UserEntity user = userService.getUser(id).get();

        List<DailyScoreEntity> lst = dailyScoreService.getScore(user);

        for(int i = 0; i < lst.size(); i++){
            revenue += lst.get(i).getTodayEarnPrice();
        }

        model.addAttribute("balance", user.getBalance());
        model.addAttribute("revenue", revenue);
        model.addAttribute("daily", lst);

        return "dashboard";
    }
}
