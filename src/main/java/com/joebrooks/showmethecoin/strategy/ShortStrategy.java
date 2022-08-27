package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.strategy.buy.IBuyStrategy;
import com.joebrooks.showmethecoin.strategy.buy.MacdStrategy;
import com.joebrooks.showmethecoin.strategy.buy.RmiStrategy;
import com.joebrooks.showmethecoin.strategy.buy.WStrategy;
import com.joebrooks.showmethecoin.strategy.sell.ISellStrategy;
import com.joebrooks.showmethecoin.strategy.sell.RsiStrategy;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ShortStrategy implements IStrategy {

    private final MacdStrategy macdStrategy;
    private final WStrategy wStrategy;
    private final RmiStrategy rmiStrategy;
    private final RsiStrategy rsiStrategy;

    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<IBuyStrategy> buyStrategyList = new LinkedList<>();
        buyStrategyList.add(macdStrategy);
        buyStrategyList.add(wStrategy);
        buyStrategyList.add(rmiStrategy);


        return buyStrategyList.stream().allMatch(i -> i.isProperToBuy(candleResponses, tradeInfo));
    }

    @Override
    public boolean isProperToSellWithBenefit(List<CandleResponse> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<ISellStrategy> sellStrategyList = new LinkedList<>();
        sellStrategyList.add(rsiStrategy);

        return sellStrategyList.stream().allMatch(i -> i.isProperToSellWithBenefit(candleResponses, tradeInfo));
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleResponse> candleResponses, List<TradeInfoEntity> tradeInfo) {
        return true;
    }
}
