package com.joebrooks.showmethecoin.trade.strategy.policy.shorts.buy;


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
public class ShortBuyUsingRsi implements IBuyPolicy {

    private final RsiIndicator rsiIndicator;


    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<RsiResponse> rsiList = rsiIndicator.getRsi(candleResponses, 14);

        return rsiList.get(0).getRsi() < 35;

    }
}
