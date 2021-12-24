package com.joebrooks.showmethecoin.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoinTradeInfo {
    private int volume;
    private float price;
    private float myCash;
}