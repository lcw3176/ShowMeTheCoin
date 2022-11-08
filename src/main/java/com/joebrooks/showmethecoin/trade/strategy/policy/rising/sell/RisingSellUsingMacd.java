package com.joebrooks.showmethecoin.trade.strategy.policy.rising.sell;

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
public class RisingSellUsingMacd implements ISellPolicy {

    private final MacdIndicator macdIndicator;
    private final int START_INDEX = 1;
    private final int WATCH_COUNT = 5;

    @Override
    public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        return true;
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
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



        return macdResponseList.get(0).getMacd() < 0
                && macdResponseList.get(0).getSignal() < 0
                && fallingCount == WATCH_COUNT;
    }
}
