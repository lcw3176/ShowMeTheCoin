package com.joebrooks.showmethecoin.trade.strategy.policy.chaser;


import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ChaserBuyCore implements IBuyPolicy {


    private static final double OFFSET = 0.002;
    private static final double UNDER1000 = 1.0085;
    private static final double UNDER10000 = 1.0065;
    private static final double UNDER100000 = 1.0045;
    private static final double UNDER1000000 = 1.0025;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo){
        double beforePrice = candleResponses.get(1).getTradePrice();
        double nowPrice = candleResponses.get(0).getTradePrice();

        double beforeVolume = candleResponses.get(1).getAccTradeVolume();
        double nowVolume = candleResponses.get(0).getAccTradeVolume();
        double raisePercentage = nowPrice / beforePrice;

        double threshold = 100D;

        if(nowPrice < 1000){
            threshold = UNDER1000;
        } else if(nowPrice < 10000){
            threshold = UNDER10000;
        } else if(nowPrice < 100000){
            threshold = UNDER100000;
        } else if(nowPrice < 1000000){
            threshold = UNDER1000000;
        }

        return raisePercentage >= threshold && raisePercentage <= threshold + OFFSET
                && nowVolume >= beforeVolume;
    }
}
