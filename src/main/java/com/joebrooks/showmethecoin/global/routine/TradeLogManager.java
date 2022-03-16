package com.joebrooks.showmethecoin.global.routine;

import com.joebrooks.showmethecoin.global.trade.TradeResult;
import com.joebrooks.showmethecoin.global.trade.TradeStatus;
import com.joebrooks.showmethecoin.repository.trade.TradeEntity;
import com.joebrooks.showmethecoin.repository.trade.TradeService;
import com.joebrooks.showmethecoin.repository.user.UserService;
import com.joebrooks.showmethecoin.upbit.account.AccountResponse;
import com.joebrooks.showmethecoin.upbit.account.AccountService;
import com.joebrooks.showmethecoin.upbit.order.CheckOrderRequest;
import com.joebrooks.showmethecoin.upbit.order.CheckOrderResponse;
import com.joebrooks.showmethecoin.upbit.order.OrderService;
import com.joebrooks.showmethecoin.upbit.client.Side;
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

    private final UserService userService;
    private final TradeService tradeService;
    private final OrderService orderService;
    private final AccountService accountService;

    @Scheduled(cron = "0 */30 * * * *")
    public void refreshCompletedOrder(){

        userService.getAllUser().forEach(user -> {
            Page<TradeEntity> pages = tradeService.getTradeLogs(user, 0);
            List<CheckOrderResponse> responses = orderService.checkOrder(CheckOrderRequest.builder()
                    .state(TradeStatus.DONE.toString().toLowerCase())
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
                            .userId(user)
                            .tradeResult(result)
                            .sellPrice(sell)
                            .buyPrice(buy)
                            .status(TradeStatus.DONE)
                            .coinType(user.getTradeCoin())
                            .build());

                    AccountResponse accountResponse = Arrays.stream(accountService.getAccountData())
                            .filter(data -> data.getCurrency().equals("KRW"))
                            .findFirst()
                            .orElseThrow(() ->{
                                throw  new RuntimeException("계좌 정보가 없습니다");
                            });

                    user.changeBalance(Double.parseDouble(accountResponse.getBalance()));
                    userService.save(user);
                    break;
                }
            }
        });

    }
}
