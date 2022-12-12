package com.joebrooks.showmethecoin.trade;

import java.time.LocalDateTime;

public interface PriceResponse {
    CompanyType getCompanyType();

    String getMarket();

    LocalDateTime getTradeDateTimeKst();

    double getTradePrice();
}
