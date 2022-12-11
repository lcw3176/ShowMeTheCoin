package com.joebrooks.showmethecoin.trade.strategy.policy.grid;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GridBuyCore implements IBuyPolicy {


//    @Override
//    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
//        List<CandleStoreEntity> recent = candleResponses.subList(1, (candleResponses.size() + 1) / 2);
//        List<CandleStoreEntity> past = candleResponses.subList((candleResponses.size() + 1) / 2, candleResponses.size());
//
//        CandleStoreEntity recentLowPrices = recent.stream()
//                .min(Comparator.comparing(CandleStoreEntity::getTradePrice)).get();
//
//
//        CandleStoreEntity pastLowPrices =  past.stream()
//                .min(Comparator.comparing(CandleStoreEntity::getTradePrice)).get();
//
//
//        Date recentValue = TimeFormatter.parse(recentLowPrices.getDateKst());
//        Date pastValue = TimeFormatter.parse(pastLowPrices.getDateKst());
//        Date nowValue = TimeFormatter.parse(candleResponses.get(0).getDateKst());
//
//        long diff;
//        long nowDiff;
//        double yLength;
//
//        diff = recentValue.getTime() - pastValue.getTime();
//        nowDiff = nowValue.getTime() - pastValue.getTime();
//        yLength = Math.max(recentLowPrices.getTradePrice(), pastLowPrices.getTradePrice())
//                - Math.min(recentLowPrices.getTradePrice(), pastLowPrices.getTradePrice());
//
//
//        // 30분 기준
//        long xLength = (diff / 1000) / 900;
//        long nowLength = (nowDiff / 1000) / 900;
//
//
//        return Math.max(candleResponses.get(0).getTradePrice(), pastLowPrices.getTradePrice())
//                - Math.min(candleResponses.get(0).getTradePrice(), pastLowPrices.getTradePrice())
//                <= yLength / xLength * nowLength;
//    }

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        double beforeTurningPrice = candleResponses.stream()
                .limit(10)
                .min(Comparator.comparing(CandleStoreEntity::getTradePrice))
                .map(i -> i.getTradePrice())
                .get();

        int count = 0;
        boolean statusChanged = true;

        for(int i = 1; i < candleResponses.size(); i++){
            double price = candleResponses.get(i).getTradePrice();

            if(statusChanged && price / beforeTurningPrice > 1 + (0.03)){
                count++;
                statusChanged = false;
                beforeTurningPrice = price;
                continue;
            }

            if(!statusChanged && price / beforeTurningPrice < 1 - (0.03)){
                count++;
                statusChanged = true;
                beforeTurningPrice = price;
            }
        }

        return count >= 3;
    }
}
