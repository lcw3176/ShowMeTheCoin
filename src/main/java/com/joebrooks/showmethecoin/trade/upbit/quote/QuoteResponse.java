package com.joebrooks.showmethecoin.trade.upbit.quote;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuoteResponse {


    @JsonProperty("market")
    private String market;

    // 해당 캔들에서 마지막 틱이 저장된 시각
    @JsonProperty("timestamp")
    private Long timeStamp;

    // 호가 매도 총 잔량
    @JsonProperty("total_ask_size")
    private Double totalAskSize;

    // 	호가 매수 총 잔량
    @JsonProperty("total_bid_size")
    private Double totalBidSize;

}
