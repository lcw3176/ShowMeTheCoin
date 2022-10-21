package com.joebrooks.showmethecoin.trade.strategy.policy.shorts.buy;


import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdIndicator;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdResponse;
import com.joebrooks.showmethecoin.trade.indicator.rsi.RsiIndicator;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShortBuyUsingMacd implements IBuyPolicy {

    private final MacdIndicator macdIndicator;
    private final RsiIndicator rsiIndicator;


    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<MacdResponse> macdResponseList = macdIndicator.getMacd(candleResponses);

        boolean buySignal = false;

        double latestMacdValue = Math.round((macdResponseList.get(0).getMacd() * 100) / 100) ;
        double latestSignalValue = Math.round((macdResponseList.get(0).getSignal() * 100) / 100);

        double priceLine = Math.round((candleResponses.get(0).getTradePrice() / 100 / 4) * 100 / 100);
        double macd = latestMacdValue / priceLine;
        double signal = latestSignalValue / priceLine;
        double result = Math.min(macd, signal) / Math.max(macd, signal);

        if (result > 1.4 && result < 1.5) {
            buySignal = true;
        }


        return buySignal
                && latestMacdValue < 0
                && latestSignalValue < 0;

    }
}
