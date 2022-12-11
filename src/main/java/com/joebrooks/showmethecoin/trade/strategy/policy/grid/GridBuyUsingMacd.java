package com.joebrooks.showmethecoin.trade.strategy.policy.grid;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdIndicator;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdResponse;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GridBuyUsingMacd implements IBuyPolicy {

    private final MacdIndicator macdIndicator;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<MacdResponse> macdResponseList = macdIndicator.getMacd(candleResponses);


        return  macdResponseList.get(0).getMacd() < 0
                && macdResponseList.get(0).getSignal() < 0
                && macdResponseList.get(1).getMacd() > macdResponseList.get(1).getSignal()
                && macdResponseList.get(0).getMacd() < macdResponseList.get(0).getSignal();
    }

}
