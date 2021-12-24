package com.joebrooks.showmethecoin.rountine;

import com.joebrooks.showmethecoin.domain.DailyCoinScore;
import com.joebrooks.showmethecoin.service.MakeMessageService;
import com.joebrooks.showmethecoin.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailReport {

    private final MakeMessageService makeMessageService;
    private final SlackService slackService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void dailyReport(){
        float benefit = DailyCoinScore.getBenefit();
        float selling = DailyCoinScore.getSellingMoney();
        float buying = DailyCoinScore.getBuyingMoney();

        String stringifyJsonMessage = makeMessageService.makeDailyMessage(benefit, selling, buying);
        slackService.sendMessage(stringifyJsonMessage);
        DailyCoinScore.initData();
    }
}
