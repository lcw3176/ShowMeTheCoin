package com.joebrooks.showmethecoin.trade.strategy.policy.rising.buy;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.bollingerbands.BollingerBandsIndicator;
import com.joebrooks.showmethecoin.trade.indicator.bollingerbands.BollingerBandsResponse;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RisingBuyUsingBollingerBands implements IBuyPolicy {

    private final BollingerBandsIndicator bollingerBandsIndicator;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<BollingerBandsResponse> lst = bollingerBandsIndicator.getBollingerBands(candleResponses);

        return lst.get(0).getLower() <= candleResponses.get(0).getTradePrice()
                && lst.get(0).getMiddle() >= candleResponses.get(0).getTradePrice();
    }
}
