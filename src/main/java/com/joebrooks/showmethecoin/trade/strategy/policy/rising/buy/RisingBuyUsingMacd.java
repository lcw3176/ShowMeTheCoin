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
    private final int START_INDEX = 2;
    private final int WATCH_COUNT = 10;
    private final int HEAD_INDEX = 1;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<MacdResponse> macdResponseList = macdIndicator.getMacd(candleResponses);

        int risingCount = 0;
        int fallingCount = 0;

        for(int i = START_INDEX; i < WATCH_COUNT + START_INDEX; i++){
            if (macdResponseList.get(i).getMacd() > macdResponseList.get(i + 1).getMacd()
                    && macdResponseList.get(i).getSignal() > macdResponseList.get(i + 1).getSignal()){
                risingCount++;
            } else {
                fallingCount++;
            }
        }


        return macdResponseList.get(HEAD_INDEX).getMacd() > macdResponseList.get(HEAD_INDEX + 1).getMacd()
                && macdResponseList.get(HEAD_INDEX).getSignal() > macdResponseList.get(HEAD_INDEX + 1).getSignal()
                && risingCount > fallingCount;
    }
}
