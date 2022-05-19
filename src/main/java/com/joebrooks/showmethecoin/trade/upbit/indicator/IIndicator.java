package com.joebrooks.showmethecoin.trade.upbit.indicator;

import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;

import java.util.List;

public interface IIndicator {
    IndicatorResponse execute(List<CandleResponse> candles);
}
