package com.joebrooks.showmethecoin.upbitTrade.order;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CheckOrderRequest {

    @JsonProperty("state")
    private String state;
}
