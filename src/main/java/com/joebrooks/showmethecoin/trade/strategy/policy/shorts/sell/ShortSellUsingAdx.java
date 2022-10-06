package com.joebrooks.showmethecoin.trade.strategy.policy.shorts.sell;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.adx.AdxIndicator;
import com.joebrooks.showmethecoin.trade.indicator.adx.AdxResponse;
import com.joebrooks.showmethecoin.trade.strategy.policy.ISellPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShortSellUsingAdx implements ISellPolicy {

    private final AdxIndicator adxIndicator;

    @Override
    public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<AdxResponse> lst = adxIndicator.getAdx(candleResponses);

        int sellCount = 0;

        for(int i = 0; i < 5; i++){
            if(lst.get(i + 1).getPlusDI() > lst.get(i).getPlusDI()
                    && lst.get(i + 1).getMinusDI() < lst.get(i).getMinusDI()){
                sellCount++;
            }
        }
        return sellCount >= 3;
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        return true;
    }
}
