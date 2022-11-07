package com.joebrooks.showmethecoin.trade.strategy.type;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.strategy.IStrategy;
import com.joebrooks.showmethecoin.trade.strategy.policy.PolicyService;
import com.joebrooks.showmethecoin.trade.strategy.policy.resistance.buy.ResistanceBuyUsingCandle;
import com.joebrooks.showmethecoin.trade.strategy.policy.resistance.buy.ResistanceBuyUsingMacd;
import com.joebrooks.showmethecoin.trade.strategy.policy.resistance.buy.ResistanceBuyUsingRmi;
import com.joebrooks.showmethecoin.trade.strategy.policy.resistance.buy.ResistanceBuyUsingRsi;
import com.joebrooks.showmethecoin.trade.strategy.policy.resistance.sell.ResistanceSellUsingMacd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResistanceStrategy implements IStrategy {


    private final PolicyService policyService;
    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {

        return policyService
                .getBuyPolicy(ResistanceBuyUsingMacd.class, ResistanceBuyUsingRsi.class, ResistanceBuyUsingCandle.class , ResistanceBuyUsingRmi.class)
                .stream()
                .allMatch(i -> i.isProperToBuy(candleResponses, tradeInfo));
    }

    @Override
    public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {

        return policyService
                .getSellPolicy(ResistanceSellUsingMacd.class)
                .stream()
                .allMatch(i -> i.isProperToSellWithBenefit(candleResponses, tradeInfo));
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        return policyService
                .getSellPolicy(ResistanceSellUsingMacd.class)
                .stream()
                .allMatch(i -> i.isProperToSellWithLoss(candleResponses, tradeInfo));
    }
}
