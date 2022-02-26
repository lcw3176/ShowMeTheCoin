package com.joebrooks.showmethecoin.application.indicator;

import com.joebrooks.showmethecoin.domain.indicator.Indicator;
import com.joebrooks.showmethecoin.infra.upbit.coin.CoinType;

public interface IIndicator {
    <T>Indicator execute(CoinType coinType);
}
