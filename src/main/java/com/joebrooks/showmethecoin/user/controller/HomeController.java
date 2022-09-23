package com.joebrooks.showmethecoin.user.controller;

import com.joebrooks.showmethecoin.repository.dailyscore.DailyScoreEntity;
import com.joebrooks.showmethecoin.repository.dailyscore.DailyScoreService;
import com.joebrooks.showmethecoin.user.model.HomeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final DailyScoreService dailyScoreService;

    @GetMapping("/")
    public String redirectDashboard(){

        return "redirect:/home";
    }

    @GetMapping("/home")
    public String showDashboard(Model model){
        List<HomeResponse> homeResponses = new LinkedList<>();
        String[] rank = {"1st", "2nd", "3rd"};
        int index = 0;

        for(DailyScoreEntity i : dailyScoreService.getTopThree()){
            String id = i.getUser().getUserId();
            StringBuilder sb = new StringBuilder();

            sb.append(id, 0, id.length() / 2);
            sb.append("*******");

            homeResponses.add(HomeResponse.builder()
                    .nickName(sb.toString())
                    .rank(rank[index++])
                    .money(i.getTodayEarnPrice())
                    .build());
        }


        model.addAttribute("userList", homeResponses);

        return "home";
    }

}
