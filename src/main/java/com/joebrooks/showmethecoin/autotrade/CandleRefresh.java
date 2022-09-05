//package com.joebrooks.showmethecoin.autotrade;
//
//import com.joebrooks.showmethecoin.autotrade.upbit.CoinType;
//import com.joebrooks.showmethecoin.autotrade.upbit.UpbitUtil;
//import com.joebrooks.showmethecoin.autotrade.upbit.candles.CandleService;
//import com.joebrooks.showmethecoin.autotrade.upbit.ticker.TickerResponse;
//import com.joebrooks.showmethecoin.autotrade.upbit.ticker.TickerService;
//import com.joebrooks.showmethecoin.repository.candle.CandleStoreService;
//import com.joebrooks.showmethecoin.repository.userconfig.UserConfigService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.Comparator;
//import java.util.Map;
//import java.util.TreeMap;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class CandleRefresh {
//
//
//    private final CandleService candleService;
//    private final UserConfigService userConfigService;
//    private final TickerService tickerService;
//    private final CandleStoreService candleStoreService;
//
//    @PostConstruct
//    private void init(){
//        setBlackList();
//        setWhiteList();
//    }
//
//    private void setBlackList(){
//        CoinManager.BLACK_LIST.add(CoinType.XRP);
//        CoinManager.BLACK_LIST.add(CoinType.SAND);
//    }
//
//    private void setWhiteList(){
//        int whiteListSize = 20;
//        int delayMillis = 100;
//
//        CoinManager.WHITE_LIST.clear();
//
//        Map<Double, CoinType> tempMap = new TreeMap<>(Comparator.reverseOrder());
//
//        for(CoinType coinType : CoinType.values()){
//            TickerResponse response = tickerService.getTicker(coinType);
//            tempMap.put(response.getAccTradePrice24h(), coinType);
//
//            UpbitUtil.delay(delayMillis);
//        }
//
//        for(Map.Entry<Double, CoinType> coinTypeEntry : tempMap.entrySet()){
//            if(!CoinManager.BLACK_LIST.contains(coinTypeEntry.getValue())){
//                CoinManager.WHITE_LIST.add(coinTypeEntry.getValue());
//            }
//
//            if(CoinManager.WHITE_LIST.size() >= whiteListSize){
//                break;
//            }
//        }
//
//        log.info("\n" +
//                "\n거래 대상 코인 초기화" +
//                "\n{}" +
//                "\n", CoinManager.WHITE_LIST.toString());
//    }
//
//
//
//    @Scheduled(cron = "0 0 0/3 * * *", zone = "Asia/Seoul")
//    public void resetWhiteList(){
//
//        userConfigService.getAllUserConfig().forEach(user -> {
//            user.stopTrading();
//            userConfigService.save(user);
//        });
//
//
//        setWhiteList();
//        candleStoreService.deleteAll();
//
//        userConfigService.getAllUserConfig().forEach(user -> {
//            user.startTrading();
//            userConfigService.save(user);
//        });
//    }
//
//
//    @Scheduled(fixedDelay = 500)
//    public void refreshCandleData(){
//        int loadCandleCount = 200;
//        int delayMillis = 100;
//
//        for(CoinType coinType : CoinManager.WHITE_LIST){
//
//            userConfigService.getAllUserConfig().forEach(user -> {
//                if(!user.isTrading()){
//                    return;
//                }
//
//                candleService.saveCandles(coinType, user.getCandleMinute(), loadCandleCount);
//                UpbitUtil.delay(delayMillis);
//            });
//
//
//        }
//
//    }
//}
