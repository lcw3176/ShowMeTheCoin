package com.joebrooks.showmethecoin.trade.strategy.policy.shorts.buy;


import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdIndicator;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdResponse;
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
    private final int START_INDEX = 1;
    private final int WATCH_COUNT = 5;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<MacdResponse> macdResponseList = macdIndicator.getMacd(candleResponses);

        boolean buySignal = false;
        boolean changeSignal = false;

        for(int i = START_INDEX; i < WATCH_COUNT + START_INDEX; i++){
            if (macdResponseList.get(i).getMacd() < 0
                    && macdResponseList.get(i + 2).getMacd() > macdResponseList.get(i + 1).getMacd()
                    && macdResponseList.get(i + 1).getMacd() < macdResponseList.get(i).getMacd()
                    && Math.abs(macdResponseList.get(i + 1).getMacd() / Math.abs(macdResponseList.get(i).getMacd())) >= 1.05) {
                changeSignal = true;
                break;
            }

            double latestMacdValue = Math.round(macdResponseList.get(i).getMacd());
            double latestSignalValue = Math.round(macdResponseList.get(i).getSignal());

            double priceLine = Math.round(candleResponses.get(i).getTradePrice() / 100 / 4);
            double macd = latestMacdValue / priceLine;
            double signal = latestSignalValue / priceLine;
            double result = Math.min(macd, signal) / Math.max(macd, signal);

            if (result > 1.0 && result < 1.1) {
                buySignal = true;
            }
        }



//        log.info("시간: {}, result: {}", candleResponses.get(0).getDateKst(), result);

        return buySignal
                && changeSignal;

    }
}
