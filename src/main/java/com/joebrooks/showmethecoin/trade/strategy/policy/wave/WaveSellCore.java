package com.joebrooks.showmethecoin.trade.strategy.policy.wave;

import com.joebrooks.showmethecoin.global.util.TimeFormatter;
import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.strategy.policy.ISellPolicy;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class WaveSellCore implements ISellPolicy {
    @Override
    public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<CandleStoreEntity> recent = candleResponses.subList(1, (candleResponses.size() + 1) / 2);
        List<CandleStoreEntity> past = candleResponses.subList((candleResponses.size() + 1) / 2, candleResponses.size());

        CandleStoreEntity recentLowPrices = recent.stream()
                .max(Comparator.comparing(CandleStoreEntity::getTradePrice)).get();


        CandleStoreEntity pastLowPrices =  past.stream()
                .max(Comparator.comparing(CandleStoreEntity::getTradePrice)).get();


        Date recentValue = TimeFormatter.parse(recentLowPrices.getDateKst());
        Date pastValue = TimeFormatter.parse(pastLowPrices.getDateKst());
        Date nowValue = TimeFormatter.parse(candleResponses.get(0).getDateKst());

        long diff;
        long nowDiff;
        double yLength;

        diff = recentValue.getTime() - pastValue.getTime();
        nowDiff = nowValue.getTime() - pastValue.getTime();
        yLength = recentLowPrices.getTradePrice() - pastLowPrices.getTradePrice();


        // 30분 기준
        long xLength = (diff / 1000) / 1800;
        long nowLength = (nowDiff / 1000) / 1800;


        return candleResponses.get(0).getTradePrice() - pastLowPrices.getTradePrice() >= yLength / xLength * nowLength;
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<CandleStoreEntity> recent = candleResponses.subList(1, (candleResponses.size() + 1) / 2);
        List<CandleStoreEntity> past = candleResponses.subList((candleResponses.size() + 1) / 2, candleResponses.size());

        CandleStoreEntity recentLowPrices = recent.stream()
                .min(Comparator.comparing(CandleStoreEntity::getTradePrice)).get();


        CandleStoreEntity pastLowPrices =  past.stream()
                .min(Comparator.comparing(CandleStoreEntity::getTradePrice)).get();


        Date recentValue = TimeFormatter.parse(recentLowPrices.getDateKst());
        Date pastValue = TimeFormatter.parse(pastLowPrices.getDateKst());
        Date nowValue = TimeFormatter.parse(candleResponses.get(0).getDateKst());

        long diff;
        long nowDiff;
        double yLength;

        diff = recentValue.getTime() - pastValue.getTime();
        nowDiff = nowValue.getTime() - pastValue.getTime();
        yLength = Math.max(recentLowPrices.getTradePrice(), pastLowPrices.getTradePrice())
                - Math.min(recentLowPrices.getTradePrice(), pastLowPrices.getTradePrice());


        // 30분 기준
        long xLength = (diff / 1000) / 1800;
        long nowLength = (nowDiff / 1000) / 1800;


        return Math.max(candleResponses.get(0).getTradePrice(), pastLowPrices.getTradePrice())
                - Math.min(candleResponses.get(0).getTradePrice(), pastLowPrices.getTradePrice())
                <= yLength / xLength * nowLength  * (1 + 0.005);
    }
}
