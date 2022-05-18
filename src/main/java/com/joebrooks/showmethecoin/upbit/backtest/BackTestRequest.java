package com.joebrooks.showmethecoin.upbit.backtest;

import com.joebrooks.showmethecoin.global.strategy.Strategy;
import com.joebrooks.showmethecoin.upbit.client.CoinType;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BackTestRequest {

    private CoinType tradeCoin;
    private long startBalance;
    private long startPrice;
    private long commonDifference;
    private int candleMinute;
    private Strategy strategy;
}
