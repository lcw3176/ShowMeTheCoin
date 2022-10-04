package com.joebrooks.showmethecoin.trade.strategy.type;


import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.strategy.IStrategy;
import com.joebrooks.showmethecoin.trade.strategy.policy.PolicyService;
import com.joebrooks.showmethecoin.trade.strategy.policy.shorts.buy.ShortBuyCore;
import com.joebrooks.showmethecoin.trade.strategy.policy.shorts.buy.ShortBuyUsingMacd;
import com.joebrooks.showmethecoin.trade.strategy.policy.shorts.buy.ShortBuyUsingRsi;
import com.joebrooks.showmethecoin.trade.strategy.policy.shorts.sell.ShortSellCore;
import com.joebrooks.showmethecoin.trade.strategy.policy.shorts.sell.ShortSellUsingRsi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShortStrategy implements IStrategy {

    private final PolicyService policyService;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {

        return policyService
                .getBuyPolicy(ShortBuyCore.class, ShortBuyUsingMacd.class, ShortBuyUsingRsi.class)
                .stream()
                .allMatch(i -> i.isProperToBuy(candleResponses, tradeInfo));
    }

    @Override
    public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {

        return policyService
                .getSellPolicy(ShortSellUsingRsi.class)
                .stream()
                .allMatch(i -> i.isProperToSellWithBenefit(candleResponses, tradeInfo));
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        return policyService
                .getSellPolicy(ShortSellCore.class)
                .stream()
                .allMatch(i -> i.isProperToSellWithLoss(candleResponses, tradeInfo));
    }
}
