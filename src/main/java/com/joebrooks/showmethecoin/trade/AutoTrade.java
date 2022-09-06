package com.joebrooks.showmethecoin.trade;


import com.joebrooks.showmethecoin.trade.strategy.IStrategy;
import com.joebrooks.showmethecoin.trade.strategy.StrategyService;
import com.joebrooks.showmethecoin.trade.strategy.StrategyType;
import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import com.joebrooks.showmethecoin.trade.upbit.UpbitUtil;
import com.joebrooks.showmethecoin.trade.upbit.account.AccountResponse;
import com.joebrooks.showmethecoin.trade.upbit.account.AccountService;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleService;
import com.joebrooks.showmethecoin.trade.upbit.client.OrderType;
import com.joebrooks.showmethecoin.trade.upbit.client.Side;
import com.joebrooks.showmethecoin.trade.upbit.order.*;
import com.joebrooks.showmethecoin.trade.upbit.ticker.TickerResponse;
import com.joebrooks.showmethecoin.trade.upbit.ticker.TickerService;
import com.joebrooks.showmethecoin.repository.CompanyType;
import com.joebrooks.showmethecoin.repository.account.UserAccountService;
import com.joebrooks.showmethecoin.repository.candle.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.candle.CandleStoreService;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoService;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.repository.userconfig.UserConfigEntity;
import com.joebrooks.showmethecoin.repository.userconfig.UserConfigService;
import com.joebrooks.showmethecoin.repository.userkey.UserKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoTrade {


    private final AccountService accountService;
    private final OrderService orderService;
    private final CandleStoreService candleStoreService;
    private final UserConfigService userConfigService;
    private final TradeInfoService tradeInfoService;
    private final StrategyService strategyService;
    private final UserAccountService userAccountService;
    private final UserKeyService userKeyService;

    private final CandleService candleService;

    private final TickerService tickerService;

    private static final int UPBIT_MIN_BET_MONEY = 5100;

    @PostConstruct
    private void init(){
        setBlackList();
        setWhiteList();
    }

    private void setBlackList(){
        CoinManager.BLACK_LIST.add(CoinType.XRP);
        CoinManager.BLACK_LIST.add(CoinType.SAND);
    }

    private void setWhiteList(){
        int whiteListSize = 20;
        int delayMillis = 100;

        CoinManager.WHITE_LIST.clear();

        Map<Double, CoinType> tempMap = new TreeMap<>(Comparator.reverseOrder());

        for(CoinType coinType : CoinType.values()){
            TickerResponse response = tickerService.getTicker(coinType);
            tempMap.put(response.getAccTradePrice24h(), coinType);

            UpbitUtil.delay(delayMillis);
        }

        for(Map.Entry<Double, CoinType> coinTypeEntry : tempMap.entrySet()){
            if(!CoinManager.BLACK_LIST.contains(coinTypeEntry.getValue())){
                CoinManager.WHITE_LIST.add(coinTypeEntry.getValue());
            }

            if(CoinManager.WHITE_LIST.size() >= whiteListSize){
                break;
            }
        }

        // fixme 거래 했었던 코인 중 화이트리스트 제외되었을대 대책 세우기
        userConfigService.getAllUserConfig().forEach(user -> {
            tradeInfoService.getAllTradeCoins(user.getUser()).forEach(coin -> {
                if(!CoinManager.WHITE_LIST.contains(coin)){
                    CoinManager.WHITE_LIST.add(coin);
                }
            });
        });


        log.info("\n" +
                "\n거래 대상 코인 초기화" +
                "\n{}" +
                "\n", CoinManager.WHITE_LIST.toString());
    }

    @Scheduled(cron = "0 0 0/3 * * *", zone = "Asia/Seoul")
    public void resetWhiteList(){

        userConfigService.getAllUserConfig().forEach(user -> {
            user.stopTrading();
            userConfigService.save(user);
        });


        setWhiteList();
        candleStoreService.deleteAll();

        userConfigService.getAllUserConfig().forEach(user -> {
            user.startTrading();
            userConfigService.save(user);
        });
    }

    private void initMinCash(UserConfigEntity user){
        int divideNum = user.getCashDividedCount();

        AccountResponse accountResponse = accountService.getKRWCurrency(
                userKeyService.getKeySet(user.getUser(), CompanyType.UPBIT));
        double myBalance = Math.ceil(Double.parseDouble(accountResponse.getBalance()));

        userAccountService.changeBalance(user.getUser(), myBalance);

        log.info("\n" +
                "\n현재 잔고" +
                "\n유저: {}" +
                "\n사용가능: {}" +
                "\n사용불가능: {}" +
                "\n", user.getUser().getUserId(), accountResponse.getBalance(), accountResponse.getLocked());

        double minCash = Math.ceil(myBalance / divideNum * 0.99);
        user.setBetMoney(minCash);

        log.info("\n" +
                "\n거래 금액 초기화" +
                "\n유저: {}" +
                "\n거래 최소 금액: {}" +
                "\n", user.getUser().getUserId(), minCash);


        userConfigService.save(user);
    }

    private boolean isOrderDone(UserEntity user, CompanyType companyType){
        return orderService.checkOrder(CheckOrderRequest.builder()
                .state(OrderStatus.wait)
                .build(), userKeyService.getKeySet(user, companyType)).isEmpty();
    }


    // fixme 혼자 쓰려고 만든 구조인데 여러명이 사용하게 됬다. 다중 이용자 방식에 맞춰서 다시 재설계할것
    @Scheduled(fixedDelay = 500)
    public void autoTrade() {

        int loadCandleCount = 200;
        int candleDelayMillis = 100;
        int orderDelayMillis = 150;

        try {
            userConfigService.getAllUserConfig().forEach(user -> {
                if (!user.isTrading()) {
                    return;
                }

                for(CoinType coinType : tradeInfoService.getAllTradeCoins(user.getUser())){

                    TradeInfoEntity tradeInfo = tradeInfoService.getRecentTrade(user.getUser(), coinType);

                    // 주문 완료 처리
                    if(tradeInfo.isOrdered()
                            && !tradeInfo.isCompleted()
                            && isOrderDone(user.getUser(), tradeInfo.getCompanyType())){

                        tradeInfoService.orderComplete(user.getUser(), tradeInfo.getCoinType());

                        log.info("\n" +
                                "\n주문 체결 완료" +
                                "\n유저: {}" +
                                "\n코인: {}" +
                                "\n", user.getUser().getUserId(), tradeInfo.getCoinType().getKoreanName());
                    }

                    if(tradeInfo.getOrderedAt().plusMinutes(user.getOrderCancelMinute()).isBefore(LocalDateTime.now())
                            && tradeInfo.isCompleted()){

                        tradeInfoService.removeTradeLogs(user.getUser(), tradeInfo.getCoinType());

                        log.info("\n" +
                                "\n재거래 가능" +
                                "\n유저: {}" +
                                "\n코인: {}" +
                                "\n", user.getUser().getUserId(), tradeInfo.getCoinType().getKoreanName());

                        if(tradeInfoService.getAllTrades(user.getUser()).isEmpty()){
                            initMinCash(user);
                        }

                    }

                    // 주문 취소 처리
                    if(tradeInfo.getOrderedAt().plusMinutes(user.getOrderCancelMinute()).isBefore(LocalDateTime.now())
                            && !tradeInfo.isCompleted()){

                        List<CheckOrderResponse> checkOrderResponseList = orderService.checkOrder(CheckOrderRequest.builder()
                                .state(OrderStatus.wait)
                                .build(), userKeyService.getKeySet(user.getUser(), tradeInfo.getCompanyType()));

                        for(CheckOrderResponse order : checkOrderResponseList){
                            if(order.getMarket().equals(tradeInfo.getCoinType().getName())){

                                orderService.cancelOrder(CancelOrderRequest.builder()
                                        .uuid(order.getUuid())
                                        .build(), userKeyService.getKeySet(user.getUser(), tradeInfo.getCompanyType()));


                                log.info("\n" +
                                        "\n주문 취소" +
                                        "\n유저: {}" +
                                        "\n코인: {}" +
                                        "\n", user.getUser().getUserId(), tradeInfo.getCoinType().getKoreanName());

                                // 매수 주문이면 삭제 처리
                                if(order.getSide().equals(Side.bid.toString())){

                                    log.info("\n" +
                                            "\n재거래 가능" +
                                            "\n유저: {}" +
                                            "\n코인: {}" +
                                            "\n", user.getUser().getUserId(), tradeInfo.getCoinType().getKoreanName());

                                    tradeInfoService.removeOrder(tradeInfo.getUuid());
                                }
                                // 매도 주문이면 주문 취소 처리
                                else {
                                    tradeInfoService.orderCanceled(user.getUser(), tradeInfo.getCoinType());
                                }

                            }
                        }
                    }

                    UpbitUtil.delay(orderDelayMillis);
                }


                List<IStrategy> strategyList = strategyService.get(StrategyType.BASE, user.getStrategy());

                for (CoinType coinType : CoinManager.WHITE_LIST) {

                    List<CandleStoreEntity> candles = candleService.getCandles(coinType, user.getCandleMinute(), loadCandleCount);
                    CandleStoreEntity nowCandle = candles.get(0);

                    List<TradeInfoEntity> tradeInfoEntities = tradeInfoService.getTradeLogs(user.getUser(), coinType);

                    // 구매
                    if (strategyList.stream().allMatch(st -> st.isProperToBuy(candles, tradeInfoEntities))
                        && tradeInfoEntities.isEmpty()
                        && tradeInfoService.getTradeCoinsCount(user.getUser()) < user.getMaxTradeCoinCount()) {



                        if(buyCoin(user, nowCandle, coinType, tradeInfoEntities)){
                            log.info("\n" +
                                    "\n구매 주문" +
                                    "\n유저: {}" +
                                    "\n코인: {}" +
                                    "\n가격: {}" +
                                    "\n", user.getUser().getUserId(), coinType.getKoreanName(), nowCandle.getTradePrice());

                        }

                    }

                    // 재구매 주문
                    else if (!tradeInfoEntities.isEmpty()
                            && strategyList.stream().allMatch(st -> st.isProperToSellWithLoss(candles, tradeInfoEntities))
                            && tradeInfoEntities.stream().noneMatch(TradeInfoEntity::isOrdered)
                            && tradeInfoEntities.size() <= user.getMaxBetCount()) {


                        if(buyCoin(user, nowCandle, coinType, tradeInfoEntities)){
                            log.info("\n" +
                                    "\n재구매 주문" +
                                    "\n유저: {}" +
                                    "\n코인: {}" +
                                    "\n가격: {}" +
                                    "\n", user.getUser().getUserId(), coinType.getKoreanName(), nowCandle.getTradePrice());

                        }
                    }


                    // 익절
                    else if (!tradeInfoEntities.isEmpty()
                            && strategyList.stream().allMatch(st -> st.isProperToSellWithBenefit(candles, tradeInfoEntities))
                            && tradeInfoEntities.stream().noneMatch(TradeInfoEntity::isOrdered)) {

                        if(sellCoin(user, nowCandle, coinType)){
                            log.info("\n" +
                                    "\n판매 주문" +
                                    "\n유저: {}" +
                                    "\n코인: {}" +
                                    "\n가격: {}" +
                                    "\n", user.getUser().getUserId(), coinType.getKoreanName(), nowCandle.getTradePrice());
                        }
                    }


                    // 손절
                    else if (user.isAllowSellWithLoss()
                            && !tradeInfoEntities.isEmpty()
                            && strategyList.stream().allMatch(st -> st.isProperToSellWithLoss(candles, tradeInfoEntities))
                            && tradeInfoEntities.stream().noneMatch(TradeInfoEntity::isOrdered)
                            && tradeInfoEntities.size() > user.getMaxBetCount()) {

                        if(sellCoin(user, nowCandle, coinType)){
                            log.info("\n" +
                                    "\n손절 주문" +
                                    "\n유저: {}" +
                                    "\n코인: {}" +
                                    "\n가격: {}" +
                                    "\n", user.getUser().getUserId(), coinType.getKoreanName(), nowCandle.getTradePrice());
                        }
                    }

                    UpbitUtil.delay(candleDelayMillis);
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private boolean buyCoin(UserConfigEntity user, CandleStoreEntity nowCandle, CoinType coinType, List<TradeInfoEntity> tradeInfoEntities){
        AccountResponse accountResponse = accountService.getKRWCurrency(
                userKeyService.getKeySet(user.getUser(), CompanyType.UPBIT));

        double myBalance = Math.ceil(Double.parseDouble(accountResponse.getBalance()));

        // 잔고 체크
        if (myBalance > user.getBetMoney() && myBalance >= UPBIT_MIN_BET_MONEY) {
            double tradePrice = nowCandle.getTradePrice();
            double coinVolume = user.getBetMoney() / tradePrice;

            OrderRequest orderRequest = OrderRequest.builder()
                    .market(coinType.getName())
                    .side(Side.bid)
                    .price(Double.toString(tradePrice))
                    .volume(BigDecimal.valueOf(coinVolume).toString())
                    .ordType(OrderType.limit)
                    .build();

            OrderResponse orderResponse = orderService.requestOrder(orderRequest, userKeyService.getKeySet(user.getUser(), CompanyType.UPBIT));

            TradeInfoEntity tradeInfo = TradeInfoEntity.builder()
                    .coinType(coinType)
                    .orderedAt(LocalDateTime.now())
                    .tradePrice(tradePrice)
                    .coinVolume(coinVolume)
                    .uuid(orderResponse.getUuid())
                    .betCount(tradeInfoEntities.size() + 1)
                    .user(user.getUser())
                    .ordered(false)
                    .completed(false)
                    .companyType(CompanyType.UPBIT)
                    .build();

            tradeInfoService.save(tradeInfo);

            return true;
        }

        return false;
    }

    private boolean sellCoin(UserConfigEntity user, CandleStoreEntity nowCandle, CoinType coinType){

        AccountResponse coinResponse = accountService.getCoinCurrency(
                userKeyService.getKeySet(user.getUser(), CompanyType.UPBIT), coinType);
        double coinBalance = Double.parseDouble(coinResponse.getBalance());

        if (coinBalance > 0) {
            OrderRequest orderRequest = OrderRequest.builder()
                    .market(coinType.getName())
                    .side(Side.ask)
                    .price(Double.toString(nowCandle.getTradePrice()))
                    .volume(BigDecimal.valueOf(coinBalance).toString())
                    .ordType(OrderType.limit)
                    .build();

            orderService.requestOrder(orderRequest, userKeyService.getKeySet(user.getUser(), CompanyType.UPBIT));
            tradeInfoService.acceptOrder(user.getUser(), coinType);

            return true;
        }

        return false;
    }

}
