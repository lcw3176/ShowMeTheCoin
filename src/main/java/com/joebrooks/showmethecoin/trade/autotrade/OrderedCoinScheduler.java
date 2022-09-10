//package com.joebrooks.showmethecoin.trade.autotrade;
//
//import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
//import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoService;
//import com.joebrooks.showmethecoin.repository.useraccount.UserAccountService;
//import com.joebrooks.showmethecoin.repository.userconfig.UserConfigEntity;
//import com.joebrooks.showmethecoin.repository.userconfig.UserConfigService;
//import com.joebrooks.showmethecoin.repository.userkey.UserKeyService;
//import com.joebrooks.showmethecoin.trade.CompanyType;
//import com.joebrooks.showmethecoin.trade.upbit.CoinType;
//import com.joebrooks.showmethecoin.trade.upbit.UpbitUtil;
//import com.joebrooks.showmethecoin.trade.upbit.account.AccountResponse;
//import com.joebrooks.showmethecoin.trade.upbit.account.AccountService;
//import com.joebrooks.showmethecoin.trade.upbit.client.Side;
//import com.joebrooks.showmethecoin.trade.upbit.order.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class OrderedCoinScheduler {
//
//    private final UserConfigService userConfigService;
//    private final TradeInfoService tradeInfoService;
//    private final OrderService orderService;
//    private final UserKeyService userKeyService;
//    private final AccountService accountService;
//    private final UserAccountService userAccountService;
//
//    @Scheduled(fixedDelay = 1000)
//    public void checkOrderedCoin(){
//        int orderDelayMillis = 150;
//
//        try{
//            userConfigService.getAllUserConfig().forEach(user -> {
//
//                for(CoinType coinType : tradeInfoService.getAllTradeCoins(user.getUser())){
//
//                    TradeInfoEntity tradeInfo = tradeInfoService.getRecentTrade(user.getUser(), coinType);
//
//                    // 주문 완료 처리
//                    if(tradeInfo.isOrdered()
//                            && !tradeInfo.isCompleted()
//                            && orderService.isEveryOrderDone(userKeyService.getKeySet(user.getUser(), tradeInfo.getCompanyType()))){
//
//                        tradeInfoService.orderComplete(user.getUser(), tradeInfo.getCoinType());
//
//                        log.info("\n" +
//                                "\n주문 체결 완료" +
//                                "\n유저: {}" +
//                                "\n코인: {}" +
//                                "\n", user.getUser().getUserId(), tradeInfo.getCoinType().getKoreanName());
//                    }
//
//                    if(tradeInfo.getOrderedAt().plusMinutes(user.getOrderCancelMinute()).isBefore(LocalDateTime.now())
//                            && tradeInfo.isCompleted()){
//
//                        tradeInfoService.removeTradeLogs(user.getUser(), tradeInfo.getCoinType());
//
//                        log.info("\n" +
//                                "\n재거래 가능" +
//                                "\n유저: {}" +
//                                "\n코인: {}" +
//                                "\n", user.getUser().getUserId(), tradeInfo.getCoinType().getKoreanName());
//
//                        if(tradeInfoService.getAllTrades(user.getUser()).isEmpty()){
//                            resetUserBetMoney(user);
//                        }
//
//                    }
//
//                    // 주문 취소 처리
//                    if(tradeInfo.getOrderedAt().plusMinutes(user.getOrderCancelMinute()).isBefore(LocalDateTime.now())
//                            && !tradeInfo.isCompleted()){
//
//                        List<CheckOrderResponse> checkOrderResponseList = orderService.checkOrder(CheckOrderRequest.builder()
//                                .state(OrderStatus.wait)
//                                .build(), userKeyService.getKeySet(user.getUser(), tradeInfo.getCompanyType()));
//
//                        for(CheckOrderResponse order : checkOrderResponseList){
//                            if(order.getMarket().equals(tradeInfo.getCoinType().getName())){
//
//                                orderService.cancelOrder(CancelOrderRequest.builder()
//                                        .uuid(order.getUuid())
//                                        .build(), userKeyService.getKeySet(user.getUser(), tradeInfo.getCompanyType()));
//
//                                log.info("\n" +
//                                        "\n주문 취소" +
//                                        "\n유저: {}" +
//                                        "\n코인: {}" +
//                                        "\n", user.getUser().getUserId(), tradeInfo.getCoinType().getKoreanName());
//
//                                // 매수 주문이면 삭제 처리
//                                if(order.getSide().equals(Side.bid.toString())){
//
//                                    tradeInfoService.removeOrder(tradeInfo.getUuid());
//
//                                    log.info("\n" +
//                                            "\n재거래 가능" +
//                                            "\n유저: {}" +
//                                            "\n코인: {}" +
//                                            "\n", user.getUser().getUserId(), tradeInfo.getCoinType().getKoreanName());
//
//                                }
//
//                                // 매도 주문이면 주문 취소 처리
//                                else {
//                                    tradeInfoService.orderCanceled(user.getUser(), tradeInfo.getCoinType());
//                                }
//
//                            }
//                        }
//                    }
//
//                    UpbitUtil.delay(orderDelayMillis);
//                }
//            });
//        } catch (Exception e){
//            log.error(e.getMessage(), e);
//        }
//
//    }
//
//
//    private void resetUserBetMoney(UserConfigEntity user){
//        int divideNum = user.getCashDividedCount();
//
//        AccountResponse accountResponse = accountService.getKRWCurrency(
//                userKeyService.getKeySet(user.getUser(), CompanyType.UPBIT));
//        double myBalance = Math.ceil(Double.parseDouble(accountResponse.getBalance()));
//
//        userAccountService.changeBalance(user.getUser(), myBalance);
//
//        log.info("\n" +
//                "\n현재 잔고" +
//                "\n유저: {}" +
//                "\n사용가능: {}" +
//                "\n사용불가능: {}" +
//                "\n", user.getUser().getUserId(), accountResponse.getBalance(), accountResponse.getLocked());
//
//        double minCash = Math.ceil(myBalance / divideNum * 0.99);
//        user.setBetMoney(minCash);
//
//        log.info("\n" +
//                "\n거래 금액 초기화" +
//                "\n유저: {}" +
//                "\n거래 최소 금액: {}" +
//                "\n", user.getUser().getUserId(), minCash);
//
//
//        userConfigService.save(user);
//    }
//
//}
