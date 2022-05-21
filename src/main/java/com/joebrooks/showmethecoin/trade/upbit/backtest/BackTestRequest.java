package com.joebrooks.showmethecoin.trade.upbit.backtest;

import com.joebrooks.showmethecoin.trade.upbit.client.CoinType;
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
    private double lossRate;

}
