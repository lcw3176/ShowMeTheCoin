package com.joebrooks.showmethecoin.exchange.upbit.order.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.joebrooks.showmethecoin.exchange.upbit.order.OrderStatus;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class CheckOrderRequest {

    @JsonProperty("state")
    @Enumerated(EnumType.STRING)
    private OrderStatus state;
}
