package com.joebrooks.showmethecoin.order;

import com.fasterxml.jackson.annotation.JsonProperty;
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
