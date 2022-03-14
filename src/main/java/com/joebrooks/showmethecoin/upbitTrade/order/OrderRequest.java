package com.joebrooks.showmethecoin.upbitTrade.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joebrooks.showmethecoin.upbitTrade.upbit.OrderType;
import com.joebrooks.showmethecoin.upbitTrade.upbit.Side;
import lombok.*;

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
