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
    private static final int START_INDEX = 1;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<MacdResponse> macdResponseList = macdIndicator.getMacd(candleResponses);

        boolean buySignal = false;

        for(int i = START_INDEX; i < START_INDEX + 1; i++){
            double latestMacdValue = Math.round((macdResponseList.get(i).getMacd() * 100) / 100) ;
            double latestSignalValue = Math.round((macdResponseList.get(i).getSignal() * 100) / 100);

            double priceLine = Math.round((candleResponses.get(i).getTradePrice() / 100 / 4 / 2) * 100 / 100);
            double macd = latestMacdValue / priceLine;
            double signal = latestSignalValue / priceLine;
            double result = Math.min(macd, signal) / Math.max(macd, signal);

            if (result > 1.1 && result < 1.2) {
                buySignal = true;
                break;
            }


//            if (result > 1.31 && result < 1.36) {
//                buySignal = true;
//                break;
//            }

        }


//        double latestMacdValue = Math.round((macdResponseList.get(1).getMacd() * 100) / 100) ;
//        double latestSignalValue = Math.round((macdResponseList.get(1).getSignal() * 100) / 100);
//
//        double priceLine = Math.round((candleResponses.get(1).getTradePrice() / 100 / 4 / 4) * 100 / 100);
//        double macd = latestMacdValue / priceLine;
//        double signal = latestSignalValue / priceLine;
//        double result = Math.min(macd, signal) / Math.max(macd, signal);
//        log.info("시간: {}, 값: {}", candleResponses.get(1).getDateKst(), result);

        return buySignal
                && Math.abs(macdResponseList.get(1).getMacd()) - Math.abs(macdResponseList.get(1).getSignal()) > Math.abs(macdResponseList.get(0).getMacd()) - Math.abs(macdResponseList.get(0).getSignal())
                && macdResponseList.get(0).getSignal() < 0
                && macdResponseList.get(0).getMacd() < 0;

    }
}
