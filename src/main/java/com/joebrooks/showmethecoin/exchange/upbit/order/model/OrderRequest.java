package com.joebrooks.showmethecoin.exchange.upbit.order.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joebrooks.showmethecoin.exchange.upbit.client.OrderType;
import com.joebrooks.showmethecoin.exchange.upbit.client.Side;
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
public class OrderRequest {

    @JsonProperty("market")
    private String market;

    @JsonProperty("side")
    private Side side;

    @JsonProperty("volume")
    private String volume;

    @JsonProperty("price")
    private String price;

    @JsonProperty("ord_type")
    private OrderType ordType;

}
