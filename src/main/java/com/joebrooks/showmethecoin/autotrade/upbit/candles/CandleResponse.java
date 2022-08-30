package com.joebrooks.showmethecoin.autotrade.upbit.candles;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CandleResponse{

    @JsonProperty("market")
    private String market;

    @JsonProperty("candle_date_time_utc")
    private String dateUtc;

    @JsonProperty("candle_date_time_kst")
    private String dateKst;

    @JsonProperty("opening_price")
    private Double openingPrice;

    @JsonProperty("high_price")
    private Double highPrice;

    @JsonProperty("low_price")
    private Double lowPrice;

    @JsonProperty("trade_price")
    private Double tradePrice;

    // 해당 캔들에서 마지막 틱이 저장된 시각
    @JsonProperty("timestamp")
    private Long timeStamp;

    // 누적 거래 금액
    @JsonProperty("candle_acc_trade_price")
    private Double accTradePrice;

    // 누적 거래량
    @JsonProperty("candle_acc_trade_volume")
    private Double accTradeVolume;

    @JsonProperty("unit")
    private Integer unit;


}
