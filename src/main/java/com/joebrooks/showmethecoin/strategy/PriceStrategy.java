package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.global.fee.FeeCalculator;
import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(StrategyAnnotation.PRICE_STRATEGY)
@RequiredArgsConstructor
@Slf4j
public class PriceStrategy implements IStrategy{

    private final int buyCount = 3;
    private final int sellCount = 4;
    private final double lossRate = 0.02;
    private final double gainRate = 0.005;

    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        int count = 0;
        CandleResponse mostRecentCandle = candleResponses.get(0); // 실 매수시 인덱스 +1

        if(mostRecentCandle.getTradePrice() - mostRecentCandle.getOpeningPrice() > 0){
            return false;
        }

        if(tradeInfo.size() != 0 && tradeInfo.get(tradeInfo.size() - 1).getDateKst().equals(mostRecentCandle.getDateKst())){
            return false;
        }

        double priceGap = Math.abs(mostRecentCandle.getTradePrice() - mostRecentCandle.getOpeningPrice());

        for(int i = 1; i < candleResponses.size(); i++){ // 실 매수시 i +1
            CandleResponse candle = candleResponses.get(i);
            double price = candle.getTradePrice() - candle.getOpeningPrice();
            

            if(price < 0 && Math.abs(price) > priceGap * 0.8) { // 음봉의 크기가 유의미하게 큰 경우
                priceGap = Math.abs(price);
                count++;
            }

            if(price > 0 && price > priceGap * 0.8){ // 양봉의 크기가 유의미하게 큰 경우
                break;
            }

            if(tradeInfo.size() != 0 && tradeInfo.get(tradeInfo.size() - 1).getDateKst().equals(candle.getDateKst())){
                break;
            }
        }


        return count >= buyCount;
    }

    @Override
    public boolean isProperToSellWithBenefit(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
//        int count = 0;
        double averagePrice = getAveragePrice(tradeInfo);

//        CandleResponse mostRecentCandle = candleResponses.get(1);
//
//        if(mostRecentCandle.getTradePrice() - mostRecentCandle.getOpeningPrice() > 0){
//            return false;
//        }
//
//        if(tradeInfo.size() != 0 && tradeInfo.get(tradeInfo.size() - 1).getDateKst().equals(mostRecentCandle.getDateKst())){
//            return false;
//        }
//
//        double priceGap = Math.abs(mostRecentCandle.getTradePrice() - mostRecentCandle.getOpeningPrice());
//
//        for(int i = 2; i < candleResponses.size(); i++){
//            CandleResponse candle = candleResponses.get(i);
//            double price = candle.getTradePrice() - candle.getOpeningPrice();
//
//
//            if(price < 0 && Math.abs(price) > priceGap * 0.7) { // 음봉의 크기가 유의미하게 큰 경우
//                break;
//            }
//
//            if(price > 0 && price > priceGap * 0.7){ // 양봉의 크기가 유의미하게 큰 경우
//                priceGap = Math.abs(price);
//                count++;
//            }
//
//            if(tradeInfo.size() != 0 && tradeInfo.get(tradeInfo.size() - 1).getDateKst().equals(candle.getDateKst())){
//                break;
//            }
//        }
//
// * (1 + gainRate)
//        return count >= sellCount && averagePrice + paidFee + getPayingFee(tradeInfo, candleResponses.get(0).getTradePrice()) < candleResponses.get(0).getTradePrice();
        return (averagePrice * 1.0005) < candleResponses.get(0).getTradePrice();
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        double averagePrice = getAveragePrice(tradeInfo);

        return (averagePrice * 1.0005) * (1 - lossRate) > candleResponses.get(0).getTradePrice(); // 실 매도시 인덱스 +1
    }

    private double getAveragePrice(List<TradeInfo> tradeInfo){
        double price = 0;
        double volume = 0;

        for(int i = 0; i < tradeInfo.size(); i++){
            price += tradeInfo.get(i).getTradePrice() * tradeInfo.get(i).getCoinVolume() * 1.0005;
            volume += tradeInfo.get(i).getCoinVolume();
        }

        return price / volume;
    }

}
