package com.joebrooks.showmethecoin.trade.strategy.policy.breakout;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdIndicator;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdResponse;
import com.joebrooks.showmethecoin.trade.strategy.policy.ISellPolicy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VolatilityBreakOutSellUsingMacd implements ISellPolicy {

    private final MacdIndicator macdIndicator;


    @Override
    public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<MacdResponse> macdResponse = macdIndicator.getMacd(candleResponses);

        return macdResponse.get(1).getMacd() > macdResponse.get(1).getSignal()
                && macdResponse.get(0).getMacd() < macdResponse.get(0).getSignal();
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        return false;
    }
}
