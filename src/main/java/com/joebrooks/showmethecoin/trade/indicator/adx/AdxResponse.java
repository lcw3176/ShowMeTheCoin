package com.joebrooks.showmethecoin.trade.indicator.adx;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdxResponse {

    private double plusDI;
    private double minusDI;
    private double adx;

}
