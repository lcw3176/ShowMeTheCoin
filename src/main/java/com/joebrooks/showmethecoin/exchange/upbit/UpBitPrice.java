package com.joebrooks.showmethecoin.exchange.upbit;

import com.joebrooks.showmethecoin.exchange.CompanyType;
import com.joebrooks.showmethecoin.exchange.PriceResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UpBitPrice implements PriceResponse {

    private CompanyType companyType;
    private String market;
    private String tradeTimeKst;
    private String tradeDateKst;
    private double tradePrice;


    @Override
    public CompanyType getCompanyType() {
        return this.companyType;
    }

    @Override
    public String getMarket() {
        return market.split("-")[1].toLowerCase();
    }

    @Override
    public double getTradePrice() {
        return this.tradePrice;
    }

    @Override
    public double getAvailableSellPrice() {
        return this.tradePrice;
    }

    @Override
    public double getAvailableBuyPrice() {
        return this.tradePrice;
    }
}
