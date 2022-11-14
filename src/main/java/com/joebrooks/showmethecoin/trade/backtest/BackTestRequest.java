package com.joebrooks.showmethecoin.trade.backtest;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joebrooks.showmethecoin.global.serializer.CalendarDeserializer;
import com.joebrooks.showmethecoin.global.serializer.CalendarSerializer;
import com.joebrooks.showmethecoin.repository.candlestore.CandleMinute;
import com.joebrooks.showmethecoin.trade.strategy.StrategyType;
import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import java.util.Calendar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
