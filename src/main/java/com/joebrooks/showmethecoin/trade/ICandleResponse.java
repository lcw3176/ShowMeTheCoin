package com.joebrooks.showmethecoin.trade;

public interface ICandleResponse {
    double getAccTradePrice();

    double getAccTradeVolume();

    long getTimeStamp();

    double getHighPrice();

    double getLowPrice();

    double getTradePrice();
}
