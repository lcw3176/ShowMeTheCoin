package com.joebrooks.showmethecoin.exchange.upbit.order.model;

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
public class CancelOrderRequest {

    @JsonProperty("uuid")
    private String uuid;
}
