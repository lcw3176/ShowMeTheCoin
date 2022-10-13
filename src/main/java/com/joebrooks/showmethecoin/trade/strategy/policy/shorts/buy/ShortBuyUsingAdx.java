package com.joebrooks.showmethecoin.trade.strategy.policy.shorts.buy;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.adx.AdxIndicator;
import com.joebrooks.showmethecoin.trade.indicator.adx.AdxResponse;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShortBuyUsingAdx  implements IBuyPolicy {

    private final AdxIndicator adxIndicator;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<AdxResponse> adxResponseList = adxIndicator.getAdx(candleResponses);

        return adxResponseList.get(2).getPlusDI() < adxResponseList.get(1).getPlusDI()
                && adxResponseList.get(2).getMinusDI() > adxResponseList.get(1).getMinusDI();
    }
}
