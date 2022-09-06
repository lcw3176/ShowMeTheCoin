package com.joebrooks.showmethecoin.trade.indicator.macd;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MacdResponse{
    private double macd;
    private double signal;
}