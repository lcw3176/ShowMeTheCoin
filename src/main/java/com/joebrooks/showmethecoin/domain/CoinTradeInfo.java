package com.joebrooks.showmethecoin.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoinTradeInfo {
    private float volume;
    private float price;
    private float myCash;
}