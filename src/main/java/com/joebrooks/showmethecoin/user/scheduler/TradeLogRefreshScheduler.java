package com.joebrooks.showmethecoin.user.scheduler;

import com.joebrooks.showmethecoin.repository.tradelog.TradeLogEntity;
import com.joebrooks.showmethecoin.repository.tradelog.TradeLogService;
import com.joebrooks.showmethecoin.repository.tradelog.TradeResult;
import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import com.joebrooks.showmethecoin.trade.upbit.client.Side;
import com.joebrooks.showmethecoin.trade.upbit.order.CheckOrderRequest;
import com.joebrooks.showmethecoin.trade.upbit.order.CheckOrderResponse;
import com.joebrooks.showmethecoin.trade.upbit.order.OrderService;
import com.joebrooks.showmethecoin.trade.upbit.order.OrderStatus;
import com.joebrooks.showmethecoin.trade.CompanyType;
import com.joebrooks.showmethecoin.user.model.TradeLogReprocessor;
import com.joebrooks.showmethecoin.repository.userconfig.UserConfigService;
import com.joebrooks.showmethecoin.repository.userkey.UserKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class TradeLogRefreshScheduler {

    private final UserConfigService userConfigService;
    private final TradeLogService tradeLogService;
    private final OrderService orderService;
    private final UserKeyService userKeyService;

    @Scheduled(cron = "0 * * * * *")
    public void refreshCompletedOrderFromUpbit(){

        userConfigService.getAllUserConfig().forEach(userConfig -> {
            List<TradeLogEntity> logList = tradeLogService.getLogsByTimeDesc(userConfig.getUser());
            LocalDateTime lastRecordedOrderDate;

            if(logList.isEmpty()){
                lastRecordedOrderDate = LocalDateTime.now().minusMinutes(5);
            } else {
                lastRecordedOrderDate = logList.get(0).getOrderEndDate();
            }
            List<CheckOrderResponse> responses = new LinkedList<>();

            responses.addAll(orderService.checkOrder(CheckOrderRequest.builder()
                    .state(OrderStatus.done)
                    .build(), userKeyService.getKeySet(userConfig.getUser(), CompanyType.UPBIT)));


            responses.addAll(orderService.checkOrder(CheckOrderRequest.builder()
                            .state(OrderStatus.cancel)
                            .build(), userKeyService.getKeySet(userConfig.getUser(), CompanyType.UPBIT)));


            Map<CoinType, TradeLogReprocessor> map = new HashMap<>();

            for (CheckOrderResponse targetResponse : responses) {

                if (Arrays.stream(CoinType.values())
                        .noneMatch(i -> i.toString().equals(targetResponse.getMarket().split("-")[1]))) {
                    continue;
                }


                if (targetResponse.getPrice() == null) {
                    targetResponse.setPriceByPaidFee();
                }


                CoinType key = CoinType.valueOf(targetResponse.getMarket().split("-")[1]);
                LocalDateTime orderTime = LocalDateTime.parse(targetResponse.getCreatedAt().split("\\+")[0]);


                if (!map.containsKey(key)) {
                    map.put(key, TradeLogReprocessor.builder()
                            .userId(userConfig.getUser())
                            .lastOrder(orderTime)
                            .sellPrice(new LinkedList<>())
                            .buyPrice(new LinkedList<>())
                            .executeVolume(BigDecimal.valueOf(0))
                            .build());
                }




                if (targetResponse.getSide().equals(Side.ask.toString())) {
                    double tradedPrice = Double.parseDouble(targetResponse.getPrice())
                            * Double.parseDouble(targetResponse.getExecuteVolume())
                            - Double.parseDouble(targetResponse.getPaidFee());

                    map.get(key).getSellPrice().add(tradedPrice);
                    map.get(key).setExecuteVolume(
                            map.get(key).getExecuteVolume().subtract(new BigDecimal(targetResponse.getExecuteVolume())));
                } else {
                    double tradedPrice = Double.parseDouble(targetResponse.getPrice())
                            * Double.parseDouble(targetResponse.getExecuteVolume())
                            + Double.parseDouble(targetResponse.getPaidFee());

                    map.get(key).getBuyPrice().add(tradedPrice);
                    map.get(key).setExecuteVolume(
                            map.get(key).getExecuteVolume().add(new BigDecimal(targetResponse.getExecuteVolume())));
                }

                if(map.get(key).getLastOrder().isBefore(orderTime)){
                    map.get(key).changeLastOrderTime(orderTime);
                }


                if(map.get(key).getExecuteVolume().compareTo(BigDecimal.ZERO) == 0){
                    if (map.get(key).getLastOrder().isBefore(lastRecordedOrderDate)
                            || map.get(key).getLastOrder().isEqual(lastRecordedOrderDate)) {

                        map.remove(key);
                        continue;
                    }

                    if (map.get(key).getBuyPrice().stream().mapToDouble(Double::doubleValue).sum() == 0
                            || map.get(key).getSellPrice().stream().mapToDouble(Double::doubleValue).sum() == 0D) {

                        map.remove(key);
                        continue;
                    }



                    double sellPrice = map.get(key).getSellPrice().stream().mapToDouble(Double::doubleValue).sum();
                    double buyPrice = map.get(key).getBuyPrice().stream().mapToDouble(Double::doubleValue).sum();



                    TradeResult result;

                    if(sellPrice > buyPrice){
                        result = TradeResult.BENEFIT;
                    } else {
                        result = TradeResult.LOSS;
                    }

                    tradeLogService.addTradeLog(TradeLogEntity.builder()
                            .user(userConfig.getUser())
                            .tradeResult(result)
                            .sellPrice(sellPrice)
                            .buyPrice(buyPrice)
                            .orderEndDate(map.get(key).getLastOrder())
                            .coinType(key)
                            .companyType(CompanyType.UPBIT)
                            .build());

                    userConfigService.save(userConfig);


                    map.remove(key);
                }
            }



        });

    }

}


