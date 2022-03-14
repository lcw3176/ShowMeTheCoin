package com.joebrooks.showmethecoin.upbitTrade.indicator.type;

import com.joebrooks.showmethecoin.upbitTrade.candles.CandleResponse;
import com.joebrooks.showmethecoin.upbitTrade.indicator.Indicator;

import java.util.List;

public interface IIndicator {
    Indicator execute(List<CandleResponse> candles);
}
