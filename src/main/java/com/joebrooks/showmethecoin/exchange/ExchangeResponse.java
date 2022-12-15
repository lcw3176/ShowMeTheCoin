package com.joebrooks.showmethecoin.exchange;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeResponse {

    private String coinName;
    private PriceResponse upBitPrice;
    private PriceResponse coinOnePrice;
    private double difference;
    private LocalDateTime lastModified;
}
