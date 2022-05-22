//package com.joebrooks.showmethecoin.global.routine;
//
//import com.joebrooks.showmethecoin.trade.TradeResult;
//import com.joebrooks.showmethecoin.repository.trade.TradeEntity;
//import com.joebrooks.showmethecoin.repository.trade.TradeService;
//import com.joebrooks.showmethecoin.repository.userConfig.UserConfigService;
//import com.joebrooks.showmethecoin.trade.upbit.client.CoinType;
//import com.joebrooks.showmethecoin.trade.upbit.client.Side;
//import com.joebrooks.showmethecoin.trade.upbit.order.CheckOrderRequest;
//import com.joebrooks.showmethecoin.trade.upbit.order.CheckOrderResponse;
//import com.joebrooks.showmethecoin.trade.upbit.order.OrderService;
//import com.joebrooks.showmethecoin.trade.upbit.order.OrderStatus;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class TradeLogManager {
//
//    private final UserConfigService userConfigService;
//    private final TradeService tradeService;
//    private final OrderService orderService;
//
//    @Scheduled(cron = "0 */30 * * * *")
//    public void refreshCompletedOrder(){
//
//        userConfigService.getAllUserConfig().forEach(userConfig -> {
//            Page<TradeEntity> pages = tradeService.getTradeLogs(userConfig.getUserId(), 0);
//            List<CheckOrderResponse> responses = orderService.checkOrder(CheckOrderRequest.builder()
//                    .state(OrderStatus.done)
//                    .build());
//
//            for(int i = 0; i < responses.size(); i++){
//                TradeEntity lastRecordedOrder = pages.getContent().get(0);
//                CheckOrderResponse targetResponse = responses.get(i);
//                LocalDateTime targetCreatedTime = LocalDateTime.parse(targetResponse.getCreatedAt().split("\\+")[0]);
//
//                if(targetCreatedTime.isBefore(lastRecordedOrder.getCreatedDate())
//                    || targetCreatedTime.isEqual(lastRecordedOrder.getCreatedDate())){
//                    break;
//                }
//
//                if(targetResponse.getSide().equals(Side.ask.toString())){
//
//                    double sell = 0D;
//                    double buy = 0D;
//
//                    for(int j = i; j < responses.size(); j++){
//                        CheckOrderResponse unknownOrder = responses.get(j);
//                        LocalDateTime unknownOrderCreatedTime = LocalDateTime.parse(unknownOrder.getCreatedAt().split("\\+")[0]);
//
//                        if(unknownOrderCreatedTime.isBefore(lastRecordedOrder.getCreatedDate())
//                                || unknownOrderCreatedTime.isEqual(lastRecordedOrder.getCreatedDate())){
//                            break;
//                        }
//
//                        if(unknownOrder.getPrice() == null){
//                            unknownOrder.setPriceByPaidFee();
//                        }
//
//                        if (unknownOrder.getSide().equals(Side.ask.toString())) {
//                            sell += Double.parseDouble(unknownOrder.getPrice())
//                                    * Double.parseDouble(unknownOrder.getExecuteVolume())
//                                    - Double.parseDouble(unknownOrder.getPaidFee());
//
//                        } else {
//                            buy += Double.parseDouble(unknownOrder.getPrice())
//                                    * Double.parseDouble(unknownOrder.getExecuteVolume())
//                                    + Double.parseDouble(unknownOrder.getPaidFee());
//                        }
//                    }
//
//                    TradeResult result;
//
//                    if(sell > buy){
//                        result = TradeResult.Benefit;
//                    } else {
//                        result = TradeResult.Loss;
//                    }
//
//                    tradeService.addTradeLog(TradeEntity.builder()
//                            .userId(userConfig.getUserId())
//                            .tradeResult(result)
//                            .sellPrice(sell)
//                            .buyPrice(buy)
//                            .createdDate(LocalDateTime.parse(targetResponse.getCreatedAt().split("\\+")[0]))
//                            .coinType(CoinType.valueOf(targetResponse.getMarket().split("-")[1]))
//                            .build());
//
//                    userConfigService.save(userConfig);
//                    break;
//                }
//            }
//        });
//
//    }
//}
