package com.joebrooks.showmethecoin.exchange.coinone;

import com.joebrooks.showmethecoin.exchange.CompanyType;
import com.joebrooks.showmethecoin.exchange.PriceResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CoinOnePrice implements PriceResponse {

    private CompanyType companyType;
    private String market;
    private long timeStamp;
    private double tradePrice;
    
}
