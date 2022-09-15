package com.joebrooks.showmethecoin.trade.strategy.policy.shorts.buy;


import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.rsi.RsiIndicator;
import com.joebrooks.showmethecoin.trade.indicator.rsi.RsiResponse;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShortBuyUsingRsi implements IBuyPolicy {

    private final RsiIndicator rsiIndicator;
//    private static final int BUY_VALUE = 20;
//    private static final int OFFSET = 10;
//    private static final int WATCH_COUNT = 10;
//    private static final int START_INDEX = 1;


    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<RsiResponse> rsiList = rsiIndicator.getRsi(candleResponses, 14);

//        int plusCount = 0;
//        int minusCount = 0;

//        for(int i = START_INDEX; i < WATCH_COUNT + START_INDEX; i++){
//            double temp = rmiLst.get(i).getRmi() - rmiLst.get(i + 1).getRmi();
//            if(temp > 0){
//                plusCount++;
//            } else {
//                minusCount++;
//            }
//        }

//        return rmiLst.get(0).getRmi() > BUY_VALUE
//                && rmiLst.get(0).getRmi() < BUY_VALUE + OFFSET
//                && plusCount > minusCount;

        return rsiList.get(0).getRsi() < 45;

    }
}
