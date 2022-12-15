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

    private String coinId;
    private String coinKoreanName;
    private String upBitPrice;
    private String coinOnePrice;
    private String biThumbPrice;
    private double difference;
    private LocalDateTime lastModified;
}
