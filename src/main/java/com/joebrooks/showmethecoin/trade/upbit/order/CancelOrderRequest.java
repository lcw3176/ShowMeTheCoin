package com.joebrooks.showmethecoin.trade.upbit.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CancelOrderRequest {

    @JsonProperty("uuid")
    private String uuid;
}
