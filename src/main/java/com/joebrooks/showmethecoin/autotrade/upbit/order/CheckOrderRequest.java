package com.joebrooks.showmethecoin.autotrade.upbit.order;


import com.fasterxml.jackson.annotation.JsonProperty;
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
