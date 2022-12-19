package com.joebrooks.showmethecoin.user.scheduler;

import com.joebrooks.showmethecoin.repository.dailyscore.DailyScoreEntity;
import com.joebrooks.showmethecoin.repository.dailyscore.DailyScoreService;
import com.joebrooks.showmethecoin.repository.tradelog.TradeLogEntity;
import com.joebrooks.showmethecoin.repository.tradelog.TradeLogService;
import com.joebrooks.showmethecoin.repository.user.UserService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyScoreRefreshScheduler {

    private final TradeLogService tradeLogService;
    private final UserService userService;
    private final DailyScoreService dailyScoreService;

    @Scheduled(cron = "0 0 0 * * *")
    public void refreshDailyScore() {

        userService.getAllUser().forEach(user -> {
            List<TradeLogEntity> tradeEntities = tradeLogService.getTradeLogs(user,
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now());

            double buy = 0D;
            double sell = 0D;

            for (TradeLogEntity tradeLogEntity : tradeEntities) {
                buy += tradeLogEntity.getBuyPrice();
                sell += tradeLogEntity.getSellPrice();
            }

            dailyScoreService.addScore(DailyScoreEntity.builder()
                    .todayEarnPrice(sell - buy)
                    .user(user)
                    .createdDate(LocalDateTime.now().minusDays(1))
                    .build());
        });


    }
}
