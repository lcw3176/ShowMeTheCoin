package com.joebrooks.showmethecoin.trade.strategy.policy.base;


import com.joebrooks.showmethecoin.global.fee.FeeCalculator;
import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.trade.strategy.policy.ISellPolicy;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class BaseSellCore implements ISellPolicy {

//    private static final double LOSS = 0.032;
    private static final double GAIN = 0.001;
//    int THROW_MINUTE = 60;


    @Override
    public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo){
        if(tradeInfo.isEmpty()){
            return false;
        }

        double averageBuyPrice = getAverageBuyPrice(tradeInfo);
        double paidFee = getPaidFee(tradeInfo);
        double averageSellPrice = getAverageSellPrice(candleResponses, tradeInfo);
        double payingFee = getPayingFee(candleResponses, tradeInfo);


        return (averageBuyPrice + paidFee + payingFee) * (1 + GAIN) < averageSellPrice;
//                || (tradeInfo.get(0).getOrderedAt().plusMinutes(THROW_MINUTE).isBefore(LocalDateTime.now())
//                && candleResponses.get(0).getTradePrice() > tradeInfo.get(0).getTradePrice());
    }


    @Override
    public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo){
        if(tradeInfo.isEmpty()){
            return false;
        }

        double LOSS;
        CandleStoreEntity candleStore = candleResponses.get(0);

        if(candleStore.getTradePrice() >= 10000000){
            LOSS = 0.02D;
        } else if(candleStore.getTradePrice() >= 1000000){
            LOSS = 0.03D;
        } else {
            LOSS = 0.04D;
        }


        double averageBuyPrice = getAverageBuyPrice(tradeInfo);
        double paidFee = getPaidFee(tradeInfo);
        double averageSellPrice = getAverageSellPrice(candleResponses, tradeInfo);
        double payingFee = getPayingFee(candleResponses, tradeInfo);

        return (averageBuyPrice + paidFee + payingFee) * (1 - LOSS) > averageSellPrice;
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
