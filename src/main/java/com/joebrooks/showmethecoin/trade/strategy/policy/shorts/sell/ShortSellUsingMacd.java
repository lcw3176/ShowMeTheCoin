package com.joebrooks.showmethecoin.trade.strategy.policy.shorts.sell;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdIndicator;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdResponse;
import com.joebrooks.showmethecoin.trade.strategy.policy.ISellPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShortSellUsingMacd implements ISellPolicy {

    private final MacdIndicator macdIndicator;

    @Override
    public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        return true;
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<MacdResponse> macdResponseList = macdIndicator.getMacd(candleResponses);

        return macdResponseList.get(0).getMacd() < 0
                && macdResponseList.get(0).getSignal() < 0;
    }
}
