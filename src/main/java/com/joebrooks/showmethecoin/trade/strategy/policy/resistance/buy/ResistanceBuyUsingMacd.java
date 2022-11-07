package com.joebrooks.showmethecoin.trade.strategy.policy.resistance.buy;

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
public class ResistanceBuyUsingMacd implements IBuyPolicy {

    private final MacdIndicator macdIndicator;
    private final int START_INDEX = 1;
    private final int WATCH_COUNT = 3;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {

        List<MacdResponse> macdResponseList = macdIndicator.getMacd(candleResponses);
        double priceLine = candleResponses.get(0).getTradePrice() / 100 / 4;
        boolean buySignal = false;

        int risingCount = 0;
        int fallingCount = 0;

        for(int i = START_INDEX; i < WATCH_COUNT + START_INDEX; i++){
            if(macdResponseList.get(i).getMacd() < 0){

                if(Math.abs(macdResponseList.get(i + 1).getMacd()) - Math.abs(macdResponseList.get(i).getMacd()) > 0){
                    risingCount++;
                } else {
                    fallingCount++;
                }

            } else {
                fallingCount++;
            }

            if(macdResponseList.get(i).getMacd() < 0
                    && macdResponseList.get(i + 2).getMacd() > macdResponseList.get(i + 1).getMacd()
                    && macdResponseList.get(i + 1).getMacd() < macdResponseList.get(i).getMacd()){
                buySignal = true;
            }
        }


        return macdResponseList.get(0).getMacd() < 0
                && macdResponseList.get(0).getSignal() < 0
                && Math.abs(macdResponseList.get(0).getSignal()) > priceLine
                && buySignal
                && risingCount > fallingCount;
    }

}
