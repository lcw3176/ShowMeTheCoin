package com.joebrooks.showmethecoin.exchange;

public interface PriceResponse {
    CompanyType getCompanyType();

    String getMarket();

    double getTradePrice();
}
