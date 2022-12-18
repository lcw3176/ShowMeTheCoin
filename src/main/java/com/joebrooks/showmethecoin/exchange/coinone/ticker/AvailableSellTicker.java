package com.joebrooks.showmethecoin.exchange.coinone.ticker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AvailableSellTicker {

    private double price;
    // 거래 가능 물량
    private double qty;
}
