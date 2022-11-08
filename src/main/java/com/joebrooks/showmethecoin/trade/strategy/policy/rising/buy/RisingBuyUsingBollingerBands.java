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
    private final int HEAD_INDEX = 1;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<BollingerBandsResponse> lst = bollingerBandsIndicator.getBollingerBands(candleResponses);

        return lst.get(HEAD_INDEX).getLower() <= candleResponses.get(HEAD_INDEX).getTradePrice()
                && lst.get(HEAD_INDEX).getMiddle() >= candleResponses.get(HEAD_INDEX).getTradePrice();
    }
}
