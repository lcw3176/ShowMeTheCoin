package com.joebrooks.showmethecoin.trade.strategy.policy.grid;

import com.joebrooks.showmethecoin.global.fee.FeeCalculator;
import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.strategy.policy.ISellPolicy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GirdSellCore implements ISellPolicy {

    private static final double GAIN = 0.005;
    private static final double LOSS = 0.01;

    @Override
    public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        if(tradeInfo.isEmpty()){
            return false;
        }

        double averageBuyPrice = getAverageBuyPrice(tradeInfo);
        double paidFee = getPaidFee(tradeInfo);
        double averageSellPrice = getAverageSellPrice(candleResponses, tradeInfo);
        double payingFee = getPayingFee(candleResponses, tradeInfo);


        return (averageBuyPrice + paidFee + payingFee) * (1 + GAIN) < averageSellPrice;
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        double lastBuyPrice = tradeInfo.get(tradeInfo.size() - 1).getTradePrice();
        double nowPrice = candleResponses.get(0).getTradePrice();

        return lastBuyPrice * (1 - LOSS) > nowPrice;
    }

    private double getAverageBuyPrice(List<TradeInfoEntity> tradeInfo){
        double price = 0;

        for(TradeInfoEntity i : tradeInfo){
            price += i.getTradePrice() * i.getCoinVolume();
        }

        return price;
    }

    private double getPaidFee(List<TradeInfoEntity> tradeInfo){
        double fee = 0;

        for(TradeInfoEntity i : tradeInfo){
            fee += FeeCalculator.calculate(i.getTradePrice(), i.getCoinVolume(), i.getCompanyType());
        }

        return fee;
    }

    private double getAverageSellPrice(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo){
        double volume = 0;

        for(TradeInfoEntity i : tradeInfo){
            volume += i.getCoinVolume();
        }

        return volume * candleResponses.get(0).getTradePrice();
    }

    private double getPayingFee(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo){
        double volume = 0;

        for(TradeInfoEntity i : tradeInfo){
            volume += i.getCoinVolume();
        }

        return FeeCalculator.calculate(candleResponses.get(0).getTradePrice(), volume, tradeInfo.get(0).getCompanyType());
    }
}
