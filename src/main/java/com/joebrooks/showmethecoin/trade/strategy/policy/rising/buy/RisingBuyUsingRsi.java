package com.joebrooks.showmethecoin.trade.strategy.policy.rising.buy;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.rsi.RsiIndicator;
import com.joebrooks.showmethecoin.trade.indicator.rsi.RsiResponse;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RisingBuyUsingRsi implements IBuyPolicy {

    private final RsiIndicator rsiIndicator;
    private final int HEAD_INDEX = 1;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<RsiResponse> rsiList = rsiIndicator.getRsi(candleResponses, 14);

        return rsiList.get(HEAD_INDEX).getRsi() > 45
                && rsiList.get(HEAD_INDEX).getRsi() < 55;
    }
}
