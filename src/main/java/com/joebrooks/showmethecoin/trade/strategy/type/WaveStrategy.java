package com.joebrooks.showmethecoin.trade.strategy.type;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.strategy.IStrategy;
import com.joebrooks.showmethecoin.trade.strategy.policy.PolicyService;
import com.joebrooks.showmethecoin.trade.strategy.policy.wave.WaveBuyCore;
import com.joebrooks.showmethecoin.trade.strategy.policy.wave.WaveSellCore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaveStrategy implements IStrategy {

    private final PolicyService policyService;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        return policyService
                .getBuyPolicy(WaveBuyCore.class)
                .stream()
                .allMatch(i -> i.isProperToBuy(candleResponses, tradeInfo));
    }

    @Override
    public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        return policyService
                .getSellPolicy(WaveSellCore.class)
                .stream()
                .allMatch(i -> i.isProperToSellWithBenefit(candleResponses, tradeInfo));
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        return policyService
                .getSellPolicy(WaveSellCore.class)
                .stream()
                .allMatch(i -> i.isProperToSellWithLoss(candleResponses, tradeInfo));
    }
}
