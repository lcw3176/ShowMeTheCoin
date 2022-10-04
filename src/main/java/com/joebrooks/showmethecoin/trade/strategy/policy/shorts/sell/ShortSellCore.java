package com.joebrooks.showmethecoin.trade.strategy.policy.shorts.sell;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.strategy.policy.ISellPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShortSellCore  implements ISellPolicy {

    @Override
    public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        return true;
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        double percentage = 0.0625;
        double pumpPercentage = 1.02;
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

        return averageValue * pumpPercentage < tradeInfo.get(tradeInfo.size() - 1).getTradePrice();
    }
}
