package com.joebrooks.showmethecoin.trade.strategy.policy.wave;

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
public class WaveSellCore implements ISellPolicy {
    private final MacdIndicator macdIndicator;

    @Override
    public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<MacdResponse> macdResponses = macdIndicator.getMacd(candleResponses);

        return macdResponses.get(2).getMacd() < macdResponses.get(1).getMacd()
                && macdResponses.get(1).getMacd() > macdResponses.get(0).getMacd();
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        return true;
    }

}
