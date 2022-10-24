package com.joebrooks.showmethecoin.trade.strategy.policy.resistance.buy;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.rsi.RsiIndicator;
import com.joebrooks.showmethecoin.trade.indicator.rsi.RsiResponse;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResistanceBuyUsingRsi implements IBuyPolicy {

    private final RsiIndicator rsiIndicator;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<RsiResponse> rsiResponses = rsiIndicator.getRsi(candleResponses, 14);

        return rsiResponses.get(0).getRsi() >= 25
                && rsiResponses.get(0).getRsi() <= 40;
    }
}
