package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@RequiredArgsConstructor
public class QuoteStrategy implements IStrategy{

//    private final QuoteService quoteService;
//
//    @Override
//    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
//        QuoteResponse response = quoteService.getQuote(candleResponses.get(0).getMarket());
//        double ask = response.getTotalAskSize(); // 호가 매도량
//        double bid = response.getTotalBidSize(); // 호가 매수량
//
//        return bid < ask && ask / 2 < bid;
//    }


}
