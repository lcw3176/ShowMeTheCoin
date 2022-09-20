package com.joebrooks.showmethecoin.trade.upbit.candles;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joebrooks.showmethecoin.trade.ICandleResponse;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CandleResponse implements ICandleResponse {

    @JsonProperty("market")
    private String market;

    @JsonProperty("candle_date_time_utc")
    private String dateUtc;

    @JsonProperty("candle_date_time_kst")
    private String dateKst;

    @JsonProperty("opening_price")
    private double openingPrice;

    @JsonProperty("high_price")
    private double highPrice;

    @JsonProperty("low_price")
    private double lowPrice;

    @JsonProperty("trade_price")
    private double tradePrice;

    // 해당 캔들에서 마지막 틱이 저장된 시각
    @JsonProperty("timestamp")
    private long timeStamp;

    // 누적 거래 금액
    @JsonProperty("candle_acc_trade_price")
    private double accTradePrice;

    // 누적 거래량
    @JsonProperty("candle_acc_trade_volume")
    private double accTradeVolume;

    @JsonProperty("unit")
    private int unit;


}
