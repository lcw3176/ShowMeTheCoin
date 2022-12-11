//package com.joebrooks.showmethecoin.trade.autotrade;
//
//import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreService;
//import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoService;
//import com.joebrooks.showmethecoin.repository.userconfig.UserConfigService;
//import com.joebrooks.showmethecoin.trade.upbit.CoinType;
//import com.joebrooks.showmethecoin.trade.upbit.ticker.TickerService;
//import javax.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class CoinFilterScheduler {
//
//    private final TickerService tickerService;
//    private final UserConfigService userConfigService;
//    private final TradeInfoService tradeInfoService;
//    private final CandleStoreService candleStoreService;
//
//    @PostConstruct
//    private void init(){
////        setBlackList();
//        setWhiteList();
//    }
//
//
//    private void setBlackList(){
//        TradingCoinList.BLACKLIST.add(CoinType.SOL);
//        TradingCoinList.BLACKLIST.add(CoinType.AAVE);
//        TradingCoinList.BLACKLIST.add(CoinType.REP);
//        TradingCoinList.BLACKLIST.add(CoinType.DOT);
//        TradingCoinList.BLACKLIST.add(CoinType.NEAR);
//        TradingCoinList.BLACKLIST.add(CoinType.SBD);
//        TradingCoinList.BLACKLIST.add(CoinType.CRO);
//    }
//
//    private void setWhiteList(){
////        int delayMillis = 100;
////
////        TradingCoinList.WHITELIST.clear();
////
////        Map<Double, CoinType> tempMap = new TreeMap<>(Comparator.reverseOrder());
////
////        for(CoinType coinType : CoinType.values()){
////            TickerResponse response = tickerService.getTicker(coinType);
////            tempMap.put(response.getAccTradePrice24h(), coinType);
////
////            UpbitUtil.delay(delayMillis);
////        }
////
////        for(Map.Entry<Double, CoinType> coinTypeEntry : tempMap.entrySet()){
////            // 거래대금 10,000백만 이상만 추가
////            if(!TradingCoinList.BLACKLIST.contains(coinTypeEntry.getValue()) && coinTypeEntry.getKey() / 1000000 >= 10000){
////                TradingCoinList.WHITELIST.add(coinTypeEntry.getValue());
////            }
////
////        }
////
////
//////        // fixme 거래 했었던 코인 중 화이트리스트 제외되었을대 대책 세우기
////        userConfigService.getAllUserConfig().forEach(user -> {
////            tradeInfoService.getAllTradeCoins(user.getUser()).forEach(coin -> {
////                if(!TradingCoinList.WHITELIST.contains(coin)){
////                    TradingCoinList.WHITELIST.add(coin);
////                }
////            });
////        });
//
//        TradingCoinList.WHITELIST.add(CoinType.DOGE);
//
//        log.info("\n" +
//                "\n거래 대상 코인 초기화" +
//                "\n{}" +
//                "\n", TradingCoinList.WHITELIST.toString());
//    }
//
////    @Scheduled(cron = "0 0 0/3 * * *", zone = "Asia/Seoul")
////    public void resetWhiteList(){
////
////        userConfigService.getAllUserConfig().forEach(user -> {
////            user.stopTrading();
////            userConfigService.save(user);
////        });
////
////
////        setWhiteList();
////        List<String> removeTarget = candleStoreService.getCandlesDistinctByMarket();
////
////        for(CoinType coinType: TradingCoinList.WHITELIST){
////            removeTarget.remove(coinType.getName());
////        }
////
////        removeTarget.forEach(candleStoreService::deleteAllByMarket);
////
////        userConfigService.getAllUserConfig().forEach(user -> {
////            user.startTrading();
////            userConfigService.save(user);
////        });
////    }
//
//}
