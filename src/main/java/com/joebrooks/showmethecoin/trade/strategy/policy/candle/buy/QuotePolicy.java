package com.joebrooks.showmethecoin.trade.strategy.policy.candle.buy;


import com.joebrooks.showmethecoin.trade.candle.CandleStoreEntity;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import com.joebrooks.showmethecoin.trade.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.upbit.quote.QuoteResponse;
import com.joebrooks.showmethecoin.trade.upbit.quote.QuoteService;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class QuotePolicy implements IBuyPolicy {

    private final QuoteService quoteService;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        QuoteResponse response = quoteService.getQuote(candleResponses.get(0).getMarket());
        double ask = response.getTotalAskSize(); // 호가 매도량
        double bid = response.getTotalBidSize(); // 호가 매수량

        return bid < ask && ask / 2 < bid;
    }


}
