package com.joebrooks.showmethecoin.exchange.coinone.ticker;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Ticker {

    private String result;
    private List<TickerResponse> tickers;
}
