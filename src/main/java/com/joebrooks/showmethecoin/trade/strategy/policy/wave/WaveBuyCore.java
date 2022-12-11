package com.joebrooks.showmethecoin.trade.strategy.policy.wave;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class WaveBuyCore implements IBuyPolicy {

//    @Override
//    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
//
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
//        long xLength = (diff / 1000) / 1800;
//        long nowLength = (nowDiff / 1000) / 1800;
//
//
//        return Math.max(candleResponses.get(0).getTradePrice(), pastLowPrices.getTradePrice())
//                - Math.min(candleResponses.get(0).getTradePrice(), pastLowPrices.getTradePrice())
//                <= yLength / xLength * nowLength  * (1 + 0.005);
//    }

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {

        CandleStoreEntity mostHighPrice = candleResponses.stream()
                .max(Comparator.comparing(CandleStoreEntity::getHighPrice))
                .orElse(CandleStoreEntity.builder()
                        .tradePrice(0D)
                        .build());

        double startX = 1;
        double startY = candleResponses.get(1).getTradePrice();


        double endX = candleResponses.indexOf(mostHighPrice) + 1D;
        double endY = mostHighPrice.getHighPrice();

        if(startY >= endY){
            return false;
        }

        double dy = 1 - (startY / endY) * 200;
        double dx = endX - startX;

        double angle = Math.atan(dy / dx) * (180.0 / Math.PI);

        System.out.println("시작: " + candleResponses.get(1).getDateKst() + ",  끝: " + mostHighPrice.getDateKst() + ",  각도: " + angle);
        return angle <= -45 && angle >= -46;
    }
}
