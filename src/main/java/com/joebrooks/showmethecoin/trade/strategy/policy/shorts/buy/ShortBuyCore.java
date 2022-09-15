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
//        double maxValue = Double.NEGATIVE_INFINITY;
        double averageValue = 0D;
        int readCount = 100;

        List<Double> holdList = new LinkedList<>();

        for (int i = 0; i < readCount; i++) {
            CandleStoreEntity temp = candleResponses.get(i);
            minValue = Math.min(temp.getTradePrice(), minValue);
//            maxValue = Math.max(temp.getTradePrice(), maxValue);
        }

        for(CandleStoreEntity temp : candleResponses){
            if(Math.min(minValue, temp.getTradePrice()) / Math.max(minValue, temp.getTradePrice()) * 100 > (100 - percentage)){
                holdList.add(temp.getTradePrice());
            }

        }

        for(Double i : holdList){
            averageValue += i;
        }

        averageValue /= holdList.size();

        return averageValue != 0D
                && Math.min(averageValue, candleResponses.get(0).getTradePrice()) / Math.max(averageValue, candleResponses.get(0).getTradePrice()) * 100 > (100 - percentage);
    }


}
