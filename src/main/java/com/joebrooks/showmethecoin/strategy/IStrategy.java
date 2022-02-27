package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.common.upbit.CoinType;

public interface IStrategy {
    <T> Strategy execute(CoinType coinType);
}
