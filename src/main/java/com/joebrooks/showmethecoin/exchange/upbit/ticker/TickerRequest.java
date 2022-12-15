package com.joebrooks.showmethecoin.exchange.upbit.ticker;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class TickerRequest {

    @JsonProperty("markets")
    private String markets;

}
