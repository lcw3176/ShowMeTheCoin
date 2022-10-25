package com.joebrooks.showmethecoin.trade.indicator.bollingerbands;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class BollingerBandsResponse {

    private double upper;
    private double middle;
    private double lower;
}
