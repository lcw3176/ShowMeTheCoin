package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class CandleStrategy implements IStrategy{

    private final int buyCount = 3;

    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        int count = 0;

        for(int i = 0; i < buyCount; i++){ // 실 매수시 i +1
            CandleResponse candle = candleResponses.get(i);

            double price = candle.getTradePrice() - candle.getOpeningPrice();

            if(price < 0) {
                count++;
            }
        }

        return count >= buyCount;
    }

}
