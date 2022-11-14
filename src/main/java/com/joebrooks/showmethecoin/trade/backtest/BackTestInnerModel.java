package com.joebrooks.showmethecoin.trade.backtest;

import com.joebrooks.showmethecoin.repository.candlestore.CandleMinute;
import com.joebrooks.showmethecoin.trade.strategy.StrategyType;
import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BackTestInnerModel {
    private CoinType tradeCoin;
    private double balance;
    private int maxBetCount;
    private StrategyType strategyType;
    private CandleMinute candleMinute;
    private double startBalance;
    private double coinBalance;
    private double cashBalance;
    private double gain;
}
