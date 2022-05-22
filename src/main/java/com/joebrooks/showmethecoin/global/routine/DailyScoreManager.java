//package com.joebrooks.showmethecoin.global.routine;
//
//import com.joebrooks.showmethecoin.repository.dailyScore.DailyScoreEntity;
//import com.joebrooks.showmethecoin.repository.dailyScore.DailyScoreService;
//import com.joebrooks.showmethecoin.repository.trade.TradeEntity;
//import com.joebrooks.showmethecoin.repository.trade.TradeService;
//import com.joebrooks.showmethecoin.repository.user.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class DailyScoreManager {
//
//    private final TradeService tradeService;
//    private final UserService userService;
//    private final DailyScoreService dailyScoreService;
//
//    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
//    public void refreshDailyScore(){
//
//        userService.getAllUser().forEach(user -> {
//            List<TradeEntity> tradeEntities = tradeService.getTradeLogs(user,
//                    LocalDateTime.now().minusDays(1),
//                    LocalDateTime.now());
//
//            double buy = 0D;
//            double sell = 0D;
//
//            for (TradeEntity tradeEntity : tradeEntities) {
//                buy += tradeEntity.getBuyPrice();
//                sell += tradeEntity.getSellPrice();
//            }
//
//            dailyScoreService.addScore(DailyScoreEntity.builder()
//                    .todayEarnPrice(sell - buy)
//                    .userId(user)
//                    .build());
//        });
//
//
//    }
//}
