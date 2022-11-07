package com.joebrooks.showmethecoin.trade.strategy.type;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.strategy.IStrategy;
import com.joebrooks.showmethecoin.trade.strategy.policy.PolicyService;
import com.joebrooks.showmethecoin.trade.strategy.policy.rising.buy.RisingBuyUsingBollingerBands;
import com.joebrooks.showmethecoin.trade.strategy.policy.rising.buy.RisingBuyUsingMacd;
import com.joebrooks.showmethecoin.trade.strategy.policy.rising.buy.RisingBuyUsingRmi;
import com.joebrooks.showmethecoin.trade.strategy.policy.rising.buy.RisingBuyUsingRsi;
import com.joebrooks.showmethecoin.trade.strategy.policy.rising.sell.RisingSellUsingBollingerBands;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RisingStrategy implements IStrategy {

    private final PolicyService policyService;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {

        return policyService
                .getBuyPolicy(RisingBuyUsingMacd.class, RisingBuyUsingRsi.class, RisingBuyUsingRmi.class, RisingBuyUsingBollingerBands.class)
                .stream()
                .allMatch(i -> i.isProperToBuy(candleResponses, tradeInfo));
    }

    @Override
    public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {

        return policyService
                .getSellPolicy(RisingSellUsingBollingerBands.class)
                .stream()
                .allMatch(i -> i.isProperToSellWithBenefit(candleResponses, tradeInfo));
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {

        return policyService
                .getSellPolicy(RisingSellUsingBollingerBands.class)
                .stream()
                .allMatch(i -> i.isProperToSellWithLoss(candleResponses, tradeInfo));
    }
}
