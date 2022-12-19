package com.joebrooks.showmethecoin.trade.strategy.policy.breakout;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdIndicator;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdResponse;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VolatilityBreakOutBuyUsingMacd implements IBuyPolicy {

    private final MacdIndicator macdIndicator;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        MacdResponse macdResponse = macdIndicator.getMacd(candleResponses).get(0);

        return macdResponse.getMacd() < 0
                && macdResponse.getSignal() < 0
                && macdResponse.getMacd() < macdResponse.getSignal();
    }
}
