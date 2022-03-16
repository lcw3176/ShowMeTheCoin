package com.joebrooks.showmethecoin.upbit.indicator;

import com.joebrooks.showmethecoin.upbit.candles.CandleResponse;
import com.joebrooks.showmethecoin.upbit.indicator.IndicatorResponse;

import java.util.List;

public interface IIndicator {
    IndicatorResponse execute(List<CandleResponse> candles);
}
