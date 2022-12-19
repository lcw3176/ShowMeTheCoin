package com.joebrooks.showmethecoin.trade.strategy.policy.breakout;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VolatilityBreakOutBuyCore implements IBuyPolicy {

    private static final double K = 0.5;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        double nowOpeningPrice = candleResponses.get(0).getOpeningPrice();
        double nowTradingPrice = candleResponses.get(0).getTradePrice();

        double beforeHighPrice = candleResponses.get(1).getHighPrice();
        double beforeLowPrice = candleResponses.get(1).getLowPrice();
        double breakoutPrice = nowOpeningPrice + (beforeHighPrice - beforeLowPrice) * K;

        return nowTradingPrice > breakoutPrice;
    }
}
