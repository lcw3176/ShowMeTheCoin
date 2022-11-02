package com.joebrooks.showmethecoin.trade.strategy.policy.rising.buy;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdIndicator;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdResponse;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RisingBuyUsingMacd implements IBuyPolicy {

    private final MacdIndicator macdIndicator;
    private final int START_INDEX = 1;
    private final int WATCH_COUNT = 5;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<MacdResponse> macdResponseList = macdIndicator.getMacd(candleResponses);
        boolean buySignal = false;
        double priceLine = candleResponses.get(0).getTradePrice() / 100 / 4;

        for(int i = START_INDEX; i < WATCH_COUNT + START_INDEX; i++){
            if (macdResponseList.get(i).getMacd() < 0
                    && macdResponseList.get(i + 2).getMacd() > macdResponseList.get(i + 1).getMacd()
                    && macdResponseList.get(i + 1).getMacd() < macdResponseList.get(i).getMacd()
                    && Math.abs(macdResponseList.get(i + 1).getMacd() / Math.abs(macdResponseList.get(i).getMacd())) >= 1.05) {
                buySignal = true;
                break;
            }

        }


        return macdResponseList.get(0).getMacd() < 0
                && macdResponseList.get(0).getSignal() < 0
                && buySignal
                && Math.abs(macdResponseList.get(0).getMacd()) < priceLine;
    }
}
