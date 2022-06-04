package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;



public class TailStrategy implements IStrategy{
    private final int tailCount = 3;

    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        int count = 0;
        CandleResponse mostRecentCandle = candleResponses.get(0);

        // 양봉이면 종료
        if(mostRecentCandle.getTradePrice() - mostRecentCandle.getOpeningPrice() > 0){
            return false;
        }

        // 마지막 거래와 같은 캔들(같은 시각)이면 종료
        if(tradeInfo.size() != 0 && tradeInfo.get(tradeInfo.size() - 1).getDateKst().equals(mostRecentCandle.getDateKst())){
            return false;
        }

        for(int i = 1; i < candleResponses.size(); i++){
            CandleResponse candle = candleResponses.get(i);
            double tail = candle.getTradePrice() - candle.getLowPrice();
            double priceGap = candle.getTradePrice() - candle.getOpeningPrice();

            if(tail > 0 && Math.abs(priceGap) * 0.5 < tail) { // 꼬리가 충분히 길게 내려온 경우
                count++;
            } else {
                break;
            }

            if(tradeInfo.size() != 0 && tradeInfo.get(tradeInfo.size() - 1).getDateKst().equals(candle.getDateKst())){
                break;
            }
        }


        return count >= tailCount;
    }

}
