package com.joebrooks.showmethecoin.strategy.buy;

import com.joebrooks.showmethecoin.indicator.macd.MacdIndicator;
import com.joebrooks.showmethecoin.indicator.macd.MacdResponse;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MacdStrategy implements IBuyStrategy {

    private final MacdIndicator macdIndicator;

    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<MacdResponse> macdResponseList = macdIndicator.getMacd(candleResponses);

        return macdResponseList.get(0).getSignal() < 0
                && macdResponseList.get(0).getMacd() < 0;
    }
}
