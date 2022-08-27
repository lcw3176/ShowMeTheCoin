package com.joebrooks.showmethecoin.strategy.sell;

import com.joebrooks.showmethecoin.indicator.rsi.RsiIndicator;
import com.joebrooks.showmethecoin.indicator.rsi.RsiResponse;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RsiStrategy implements ISellStrategy{

    private final RsiIndicator rsiIndicator;

    @Override
    public boolean isProperToSellWithBenefit(List<CandleResponse> candleResponses, List<TradeInfoEntity> tradeInfo) {
        int sellValue = 55;

        List<RsiResponse> longTermRsiLst = rsiIndicator.getRsi(candleResponses, 30);

        return longTermRsiLst.get(0).getRsi() > sellValue;
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleResponse> candleResponses, List<TradeInfoEntity> tradeInfo) {
        return true;
    }
}
