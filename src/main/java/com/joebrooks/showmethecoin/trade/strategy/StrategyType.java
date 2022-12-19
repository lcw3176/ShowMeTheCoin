package com.joebrooks.showmethecoin.trade.strategy;

import com.joebrooks.showmethecoin.repository.candlestore.CandleMinute;
import com.joebrooks.showmethecoin.trade.strategy.type.BaseStrategy;
import com.joebrooks.showmethecoin.trade.strategy.type.ChaserStrategy;
import com.joebrooks.showmethecoin.trade.strategy.type.GridStrategy;
import com.joebrooks.showmethecoin.trade.strategy.type.ResistanceStrategy;
import com.joebrooks.showmethecoin.trade.strategy.type.RisingStrategy;
import com.joebrooks.showmethecoin.trade.strategy.type.ShortStrategy;
import com.joebrooks.showmethecoin.trade.strategy.type.VolatilityBreakout;
import com.joebrooks.showmethecoin.trade.strategy.type.WaveStrategy;
import lombok.Getter;

@Getter
public enum StrategyType {
    BASE("기본적인 매매 전략, 자동으로 포함됨", BaseStrategy.class, null),
    CHASER("추격 매매 전략", ChaserStrategy.class, CandleMinute.ONE_M),
    SHORT("단타 전략", ShortStrategy.class, CandleMinute.FIFTH_M),
    RESISTANCE("변곡점 전략", ResistanceStrategy.class, CandleMinute.FIVE_M),
    RISING("상승 전략", RisingStrategy.class, CandleMinute.FIVE_M),
    WAVE("파동 전략", WaveStrategy.class, CandleMinute.FIVE_M),
    GRID("그리드 전략", GridStrategy.class, CandleMinute.FIVE_M),
    BREAK_OUT("변동성 돌파 전략", VolatilityBreakout.class, CandleMinute.FIFTH_M);


    private String description;
    private Class clazz;
    private CandleMinute candleMinute;

    private StrategyType(String description, Class clazz, CandleMinute candleMinute) {
        this.description = description;
        this.clazz = clazz;
        this.candleMinute = candleMinute;
    }
}
