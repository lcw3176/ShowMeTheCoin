package com.joebrooks.showmethecoin.trade.strategy.policy.rising.sell;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.bollingerbands.BollingerBandsIndicator;
import com.joebrooks.showmethecoin.trade.strategy.policy.ISellPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RisingSellUsingBollingerBands implements ISellPolicy {

    private final BollingerBandsIndicator bollingerBandsIndicator;

    @Override
    public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        return bollingerBandsIndicator.getBollingerBands(candleResponses).get(0).getMiddle() <= candleResponses.get(0).getTradePrice();
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        return true;
    }
}
