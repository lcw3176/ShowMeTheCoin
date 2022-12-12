package com.joebrooks.showmethecoin.trade.upbit.ticker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TickerResponse {

    // 종목 구분 코드
    @JsonProperty("market")
    private String market;

    // 최근 거래 일자(UTC)
    // 포맷: yyyyMMdd
    @JsonProperty("trade_date")
    private String tradeDate;


    // 최근 거래 시각(UTC)
    //  포맷: HHmmss
    @JsonProperty("trade_time")
    private String tradeTime;


    // 최근 거래 일자(KST)
    // 포맷: yyyyMMdd
    @JsonProperty("trade_date_kst")
    private String tradeDateKst;


    // 최근 거래 시각(KST)
    //  포맷: HHmmss
    @JsonProperty("trade_time_kst")
    private String tradeTimeKst;


    // 시가
    @JsonProperty("opening_price")
    private double openingPrice;

    // 고가
    @JsonProperty("high_price")
    private double highPrice;

    // 저가
    @JsonProperty("low_price")
    private double lowPrice;

    // 종가(현재가)
    @JsonProperty("trade_price")
    private double tradePrice;


    // 	24시간 누적 거래대금
    @JsonProperty("acc_trade_price_24h")
    private double accTradePrice24h;

}
