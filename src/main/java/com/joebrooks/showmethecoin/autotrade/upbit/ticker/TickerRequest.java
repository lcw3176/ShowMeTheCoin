package com.joebrooks.showmethecoin.autotrade.upbit.ticker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TickerRequest {

    @JsonProperty("markets")
    private String markets;

}
