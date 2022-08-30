package com.joebrooks.showmethecoin.autotrade.strategy.policy.shorts.sell;

import com.joebrooks.showmethecoin.autotrade.indicator.rsi.RsiIndicator;
import com.joebrooks.showmethecoin.autotrade.indicator.rsi.RsiResponse;
import com.joebrooks.showmethecoin.autotrade.strategy.policy.ISellPolicy;
import com.joebrooks.showmethecoin.repository.candle.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShortUsingRsi implements ISellPolicy {

    private final RsiIndicator rsiIndicator;

    @Override
    public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        int sellValue = 54;

        List<RsiResponse> longTermRsiLst = rsiIndicator.getRsi(candleResponses, 30);

        return longTermRsiLst.get(0).getRsi() > sellValue;
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        return true;
    }
}
