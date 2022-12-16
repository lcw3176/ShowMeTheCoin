package com.joebrooks.showmethecoin.exchange;

import java.time.LocalDateTime;
import java.util.Map;
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
    private Map<CompanyType, String> prices;
    private LocalDateTime lastModified;
    private double difference;
}
