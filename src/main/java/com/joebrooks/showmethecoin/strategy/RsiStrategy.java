package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component(StrategyAnnotation.RSI_STRATEGY)
public class RsiStrategy implements IStrategy {

    private final int day = 7;


    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        List<Double> rsi = getRsi(candleResponses);

        return rsi.get(1) <= 30 && rsi.get(0) > 30;
    }

    @Override
    public boolean isProperToSellWithBenefit(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        return true;
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        return true;
    }

    private List<Double> getRsi(List<CandleResponse> data){
        List<Double> rsiLst = new LinkedList<>();
        List<Double> ups = new LinkedList<>();
        List<Double> downs = new LinkedList<>();

        List<Double> au = new LinkedList<>();
        List<Double> ad = new LinkedList<>();

        for(int i = data.size() - 2; i >= 0; i--){
            double diff = data.get(i).getTradePrice() - data.get(i + 1).getTradePrice();

            if(diff > 0){
                ups.add(diff);
                downs.add(0D);
            } else {
                downs.add(Math.abs(diff));
                ups.add(0D);
            }
        }

        for(int i = 0; i < ups.size(); i++){
            if(au.size() == 0){
                au.add(ups.get(i));
                ad.add(downs.get(i));
            } else {
                au.add(((day - 1) * au.get(i - 1) + ups.get(i)) / day);
                ad.add(((day - 1) * ad.get(i - 1) + downs.get(i)) / day);
            }
        }


        for(int i = au.size() - 1; i >= 0; i--){
            double rs = au.get(i) / ad.get(i);
            double rsi = rs / (1 + rs);

            rsiLst.add(rsi * 100);
        }


        return rsiLst;
    }
}
