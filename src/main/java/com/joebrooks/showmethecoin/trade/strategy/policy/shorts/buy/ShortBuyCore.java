package com.joebrooks.showmethecoin.trade.strategy.policy.shorts.buy;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class ShortBuyCore implements IBuyPolicy {


    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        double percentage = 0.0625;
        double minValue = Double.POSITIVE_INFINITY;
        double averageValue = 0D;
        int readCount = 50;

        List<Double> holdList = new LinkedList<>();

        for (int i = 0; i < readCount; i++) {
            CandleStoreEntity temp = candleResponses.get(i);
            minValue = Math.min(temp.getLowPrice(), minValue);
        }

        holdList.add(minValue);

        for(CandleStoreEntity temp : candleResponses){
            if(minValue / temp.getLowPrice() * 100 > (100 + percentage)){
                holdList.add(temp.getLowPrice());
            }

        }

        for(Double i : holdList){
            averageValue += i;
        }

        averageValue /= holdList.size();

        return averageValue / candleResponses.get(0).getLowPrice() * 100 < (100 - percentage);
    }


}
