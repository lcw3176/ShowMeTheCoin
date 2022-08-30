package com.joebrooks.showmethecoin.autotrade.upbit.ticker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TickerResponse {


    // 	24시간 누적 거래대금
    @JsonProperty("acc_trade_price_24h")
    private double accTradePrice24h;

}
