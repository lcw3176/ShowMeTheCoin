package com.joebrooks.showmethecoin.autotrade.upbit.quote;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class QuoteRequest {

    @JsonProperty("markets")
    private String markets;

}
