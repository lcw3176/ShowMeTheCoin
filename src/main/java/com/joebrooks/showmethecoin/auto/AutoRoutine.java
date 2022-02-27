package com.joebrooks.showmethecoin.auto;

import com.joebrooks.showmethecoin.account.AccountResponse;
import com.joebrooks.showmethecoin.account.AccountService;
import com.joebrooks.showmethecoin.order.OrderRequest;
import com.joebrooks.showmethecoin.order.OrderService;
import com.joebrooks.showmethecoin.order.OrderType;
import com.joebrooks.showmethecoin.order.Side;
import com.joebrooks.showmethecoin.strategy.RecommendAction;
import com.joebrooks.showmethecoin.strategy.Strategy;
import com.joebrooks.showmethecoin.strategy.StrategyService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AutoRoutine {

    private AutoCommand autoCommand = AutoCommand.STOP;
    private CommandRequest commandRequest = null;
    private static boolean isBought = false;

    private final StrategyService strategyService;
    private final AccountService accountService;
    private final OrderService orderService;

    public void setCommand(CommandRequest command){
        this.commandRequest = command;
        autoCommand = command.getAutoCommand();
    }

    @Scheduled(fixedDelay = 1000)
    public void mainAutomationRoutine(){
        if(autoCommand.equals(AutoCommand.STOP)){
            return;
        }

        Strategy strategy = strategyService.execute(commandRequest.getStrategy(), commandRequest.getCoinType());

        if(strategy.getRecommend().equals(RecommendAction.BUY)){

            Optional<AccountResponse> accountResponse = Arrays.stream(accountService.getAccountData())
                    .filter(data -> data.getCurrency().equals("KRW"))
                    .findFirst();

            double myBalance = Double.parseDouble(accountResponse.get().getBalance());


            OrderRequest orderRequest = OrderRequest.builder()
                            .market(commandRequest.getCoinType().getName())
                    .side(Side.bid)
                    .price(Double.toString(myBalance))
                    .ordType(OrderType.price)
                    .build();

            orderService.requestOrder(orderRequest);
            isBought = true;
        }

        if(strategy.getRecommend().equals(RecommendAction.SELL) && isBought){

            Optional<AccountResponse> coinResponse = Arrays.stream(accountService.getAccountData())
                    .filter(data -> data.getCurrency().equals(commandRequest.getCoinType().getName().split("-")[1]))
                    .findFirst();

            double coinBalance = Double.parseDouble(coinResponse.get().getBalance());

            OrderRequest orderRequest = OrderRequest.builder()
                    .market(commandRequest.getCoinType().getName())
                    .side(Side.ask)
                    .volume(Double.toString(coinBalance))
                    .ordType(OrderType.market)
                    .build();

            orderService.requestOrder(orderRequest);
            isBought = false;
        }



    }
}
