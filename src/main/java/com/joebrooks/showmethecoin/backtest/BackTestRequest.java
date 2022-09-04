package com.joebrooks.showmethecoin.backtest;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joebrooks.showmethecoin.autotrade.strategy.StrategyType;
import com.joebrooks.showmethecoin.autotrade.upbit.CoinType;
import com.joebrooks.showmethecoin.repository.candle.CandleMinute;
import com.joebrooks.showmethecoin.global.serializer.CalendarDeserializer;
import com.joebrooks.showmethecoin.global.serializer.CalendarSerializer;
import lombok.*;

import java.util.Calendar;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BackTestRequest {

    private CoinType tradeCoin;
    private long startBalance;
    private int maxBetCount;
    private StrategyType strategyType;
    private CandleMinute candleMinute;

    @JsonSerialize(using = CalendarSerializer.class)
    @JsonDeserialize(using = CalendarDeserializer.class)
    private Calendar startDate;

    @JsonSerialize(using = CalendarSerializer.class)
    @JsonDeserialize(using = CalendarDeserializer.class)
    private Calendar endDate;

}
