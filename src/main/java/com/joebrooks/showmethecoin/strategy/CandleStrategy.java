package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


@Slf4j
public class CandleStrategy implements IStrategy{

    private final int buyCount = 3;
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        int count = 0;
//        CandleResponse mostRecentCandle = candleResponses.get(0); // 실 매수시 인덱스 +1
//
//
//        if(mostRecentCandle.getTradePrice() - mostRecentCandle.getOpeningPrice() > 0){
//            return false;
//        }
//

        for(int i = 0; i < buyCount; i++){ // 실 매수시 i +1
            CandleResponse candle = candleResponses.get(i);

            double price = candle.getTradePrice() - candle.getOpeningPrice();

            if(price < 0) {
                count++;
            }
        }

        if(count >= buyCount){
            return true;
//            if(tradeInfo.isEmpty()){
//                return true;
//            }
//
//            try{
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(format.parse(tradeInfo.get(0).getDateKst()));
//                calendar.add(Calendar.MINUTE, 3);
//
//                return !calendar.before(format.parse(candleResponses.get(0).getDateKst()));
//
//            } catch (ParseException e) {
//                return false;
//            }

        }

        return false;
    }

//    @Override
//    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
//        if(tradeInfo.size() > 0){
//            return tradeInfo.get(tradeInfo.size() - 1).getTradePrice() > candleResponses.get(0).getTradePrice();
//        }
//
//        return true;
//    }
}
