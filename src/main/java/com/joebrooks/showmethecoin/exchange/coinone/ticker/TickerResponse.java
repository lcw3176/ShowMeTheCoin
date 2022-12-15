package com.joebrooks.showmethecoin.exchange.coinone.ticker;

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

    @JsonProperty("target_currency")
    private String targetCurrency;

    private long timestamp;

    private double last;
}