package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;

import java.util.List;

public class VolumeStrategy implements IStrategy{


    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponseList, List<TradeInfo> tradeInfoList){

        return candleResponseList.get(1).getAccTradeVolume() * 1.5 < candleResponseList.get(0).getAccTradeVolume();
    }
}
