//package com.joebrooks.showmethecoin.trade.autotrade;
//
//import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
//import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
//import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoService;
//import com.joebrooks.showmethecoin.repository.userconfig.UserConfigEntity;
//import com.joebrooks.showmethecoin.repository.userconfig.UserConfigService;
//import com.joebrooks.showmethecoin.repository.userkey.UserKeyService;
//import com.joebrooks.showmethecoin.trade.strategy.IStrategy;
//import com.joebrooks.showmethecoin.trade.strategy.StrategyService;
//import com.joebrooks.showmethecoin.trade.strategy.StrategyType;
//import com.joebrooks.showmethecoin.exchange.upbit.CoinType;
//import com.joebrooks.showmethecoin.exchange.upbit.account.AccountResponse;
//import com.joebrooks.showmethecoin.exchange.upbit.account.AccountService;
//import com.joebrooks.showmethecoin.exchange.upbit.client.OrderType;
//import com.joebrooks.showmethecoin.exchange.upbit.client.Side;
//import com.joebrooks.showmethecoin.exchange.upbit.order.model.OrderRequest;
//import com.joebrooks.showmethecoin.exchange.upbit.order.model.OrderResponse;
//import com.joebrooks.showmethecoin.exchange.upbit.order.OrderService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.NoSuchElementException;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class AutoTradeCore {
//
//    private final UserConfigService userConfigService;
//    private final TradeInfoService tradeInfoService;
//    private final AccountService accountService;
//    private final UserKeyService userKeyService;
//    private final OrderService orderService;
//
//    @Async
//    public void execute(List<CandleStoreEntity> candles){
//
//        try {
//
//            List<UserConfigEntity> userConfigList = userConfigService.getSameStrategyUsers(strategyType);
//            List<IStrategy> strategyList = strategyService.get(StrategyType.BASE, strategyType);
//            CandleStoreEntity nowCandle = candles.get(0);
//            CoinType coinType = Arrays.stream(CoinType.values())
//                    .filter(i -> i.getName().equals(nowCandle.getMarket()))
//                    .findFirst()
//                    .orElseThrow(NoSuchElementException::new);
//
//            userConfigList.forEach(user -> {
//                if(!user.isTrading()){
//                    return;
//                }
//
//                List<TradeInfoEntity> tradeInfoEntities = tradeInfoService.getTradeLogs(user.getUser(), coinType);
//
//                // 구매
//                if (strategyList.stream().allMatch(st -> st.isProperToBuy(candles, tradeInfoEntities))
//                        && tradeInfoEntities.isEmpty()
//                        && tradeInfoService.getTradeCoinsCount(user.getUser()) < user.getMaxTradeCoinCount()) {
//
//
//                    if(buyCoin(user, nowCandle, coinType, tradeInfoEntities)){
//                        log.info("\n" +
//                                "\n구매 주문" +
//                                "\n유저: {}" +
//                                "\n코인: {}" +
//                                "\n가격: {}" +
//                                "\n", user.getUser().getUserId(), coinType.getKoreanName(), nowCandle.getTradePrice());
//
//                    }
//
//                }
//
//                // 재구매 주문
//                else if (!tradeInfoEntities.isEmpty()
//                        && strategyList.stream().allMatch(st -> st.isProperToSellWithLoss(candles, tradeInfoEntities))
//                        && tradeInfoEntities.stream().noneMatch(TradeInfoEntity::isOrdered)
//                        && tradeInfoEntities.size() <= user.getMaxBetCount()) {
//
//
//                    if(buyCoin(user, nowCandle, coinType, tradeInfoEntities)){
//                        log.info("\n" +
//                                "\n재구매 주문" +
//                                "\n유저: {}" +
//                                "\n코인: {}" +
//                                "\n가격: {}" +
//                                "\n", user.getUser().getUserId(), coinType.getKoreanName(), nowCandle.getTradePrice());
//
//                    }
//                }
//
//
//                // 익절
//                else if (!tradeInfoEntities.isEmpty()
//                        && strategyList.stream().allMatch(st -> st.isProperToSellWithBenefit(candles, tradeInfoEntities))
//                        && tradeInfoEntities.stream().noneMatch(TradeInfoEntity::isOrdered)) {
//
//                    if(sellCoin(user, nowCandle, coinType)){
//                        log.info("\n" +
//                                "\n판매 주문" +
//                                "\n유저: {}" +
//                                "\n코인: {}" +
//                                "\n가격: {}" +
//                                "\n", user.getUser().getUserId(), coinType.getKoreanName(), nowCandle.getTradePrice());
//                    }
//                }
//
//
//                // 손절
//                else if (user.isAllowSellWithLoss()
//                        && !tradeInfoEntities.isEmpty()
//                        && strategyList.stream().allMatch(st -> st.isProperToSellWithLoss(candles, tradeInfoEntities))
//                        && tradeInfoEntities.stream().noneMatch(TradeInfoEntity::isOrdered)
//                        && tradeInfoEntities.size() > user.getMaxBetCount()) {
//
//                    if(sellCoin(user, nowCandle, coinType)){
//                        log.info("\n" +
//                                "\n손절 주문" +
//                                "\n유저: {}" +
//                                "\n코인: {}" +
//                                "\n가격: {}" +
//                                "\n", user.getUser().getUserId(), coinType.getKoreanName(), nowCandle.getTradePrice());
//                    }
//                }
//            });
//
//
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage(), e);
//        }
//    }
//
//
//    private boolean buyCoin(UserConfigEntity user, CandleStoreEntity nowCandle, CoinType coinType, List<TradeInfoEntity> tradeInfoEntities){
//        AccountResponse accountResponse = accountService.getKRWCurrency(
//                userKeyService.getKeySet(user.getUser(), nowCandle.getCompanyType()));
//
//        double myBalance = Math.ceil(Double.parseDouble(accountResponse.getBalance()));
//
//        // 잔고 체크
//        if (myBalance > user.getBetMoney() && myBalance >= nowCandle.getCompanyType().getMinBetMoney()) {
//            double tradePrice = nowCandle.getTradePrice();
//            double coinVolume = user.getBetMoney() / tradePrice;
//
//            OrderRequest orderRequest = OrderRequest.builder()
//                    .market(coinType.getName())
//                    .side(Side.bid)
//                    .price(Double.toString(tradePrice))
//                    .volume(BigDecimal.valueOf(coinVolume).toString())
//                    .ordType(OrderType.limit)
//                    .build();
//
//            OrderResponse orderResponse = orderService.requestOrder(orderRequest, userKeyService.getKeySet(user.getUser(), nowCandle.getCompanyType()));
//
//            TradeInfoEntity tradeInfo = TradeInfoEntity.builder()
//                    .coinType(coinType)
//                    .orderedAt(LocalDateTime.now())
//                    .tradePrice(tradePrice)
//                    .coinVolume(coinVolume)
//                    .uuid(orderResponse.getUuid())
//                    .betCount(tradeInfoEntities.size() + 1)
//                    .user(user.getUser())
//                    .ordered(false)
//                    .completed(false)
//                    .companyType(nowCandle.getCompanyType())
//                    .build();
//
//            tradeInfoService.save(tradeInfo);
//
//            return true;
//        }
//
//        return false;
//    }
//
//    private boolean sellCoin(UserConfigEntity user, CandleStoreEntity nowCandle, CoinType coinType){
//        AccountResponse coinResponse = accountService.getCoinCurrency(
//                userKeyService.getKeySet(user.getUser(), nowCandle.getCompanyType()), coinType);
//        double coinBalance = Double.parseDouble(coinResponse.getBalance());
//
//        if (coinBalance > 0) {
//            OrderRequest orderRequest = OrderRequest.builder()
//                    .market(coinType.getName())
//                    .side(Side.ask)
//                    .price(Double.toString(nowCandle.getTradePrice()))
//                    .volume(BigDecimal.valueOf(coinBalance).toString())
//                    .ordType(OrderType.limit)
//                    .build();
//
//            orderService.requestOrder(orderRequest, userKeyService.getKeySet(user.getUser(), nowCandle.getCompanyType()));
//            tradeInfoService.acceptOrder(user.getUser(), coinType);
//
//            return true;
//        }
//
//        return false;
//    }
//}
