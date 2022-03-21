package com.joebrooks.showmethecoin.global.routine;

import com.joebrooks.showmethecoin.global.trade.TradeResult;
import com.joebrooks.showmethecoin.repository.trade.TradeEntity;
import com.joebrooks.showmethecoin.repository.trade.TradeService;
import com.joebrooks.showmethecoin.repository.userConfig.UserConfigService;
import com.joebrooks.showmethecoin.upbit.account.AccountResponse;
import com.joebrooks.showmethecoin.upbit.account.AccountService;
import com.joebrooks.showmethecoin.upbit.client.Side;
import com.joebrooks.showmethecoin.upbit.order.CheckOrderRequest;
import com.joebrooks.showmethecoin.upbit.order.CheckOrderResponse;
import com.joebrooks.showmethecoin.upbit.order.OrderService;
import com.joebrooks.showmethecoin.upbit.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TradeLogManager {

    private final UserConfigService userConfigService;
    private final TradeService tradeService;
    private final OrderService orderService;
    private final AccountService accountService;

    @Scheduled(cron = "0 */30 * * * *")
    public void refreshCompletedOrder(){

        userConfigService.getAllUserConfig().forEach(userConfig -> {
            Page<TradeEntity> pages = tradeService.getTradeLogs(userConfig.getUserId(), 0);
            List<CheckOrderResponse> responses = orderService.checkOrder(CheckOrderRequest.builder()
                    .state(OrderStatus.done)
                    .build());

            for(int i = 0; i < responses.size(); i++){
                TradeEntity lastRecordedOrder = pages.getContent().get(0);

                if(LocalDateTime.parse(responses.get(i).getCreatedAt().split("\\+")[0])
                        .isBefore(lastRecordedOrder.getCreatedDate())){
                    break;
                }

                if(responses.get(i).getSide().equals(Side.ask.toString())){

                    double sell = 0D;
                    double buy = 0D;

                    for(int j = i; j < responses.size(); j++){
                        CheckOrderResponse unknownOrder = responses.get(j);

                        if(LocalDateTime.parse(unknownOrder.getCreatedAt().split("\\+")[0])
                                .isBefore(lastRecordedOrder.getCreatedDate())
                                && j != i){
                            break;
                        }

                        if(unknownOrder.getPrice() == null){
                            unknownOrder.setPriceByPaidFee();
                        }

                        if (unknownOrder.getSide().equals(Side.ask.toString())) {
                            sell += Double.parseDouble(unknownOrder.getPrice())
                                    * Double.parseDouble(unknownOrder.getExecuteVolume())
                                    - Double.parseDouble(unknownOrder.getPaidFee());

                        } else {
                            buy += Double.parseDouble(unknownOrder.getPrice())
                                    * Double.parseDouble(unknownOrder.getExecuteVolume())
                                    + Double.parseDouble(unknownOrder.getPaidFee());
                        }
                    }

                    TradeResult result;

                    if(sell > buy){
                        result = TradeResult.Benefit;
                    } else {
                        result = TradeResult.Loss;
                    }

                    tradeService.addTradeLog(TradeEntity.builder()
                            .userId(userConfig.getUserId())
                            .tradeResult(result)
                            .sellPrice(sell)
                            .buyPrice(buy)
                            .status(OrderStatus.done)
                            .coinType(userConfig.getTradeCoin())
                            .build());

                    userConfigService.save(userConfig);
                    break;
                }
            }
        });

    }
}
