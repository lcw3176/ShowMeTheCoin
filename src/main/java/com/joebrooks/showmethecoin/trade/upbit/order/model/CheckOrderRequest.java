package com.joebrooks.showmethecoin.trade.upbit.order.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.joebrooks.showmethecoin.trade.upbit.order.OrderStatus;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CheckOrderRequest {

    @JsonProperty("state")
    @Enumerated(EnumType.STRING)
    private OrderStatus state;
}
