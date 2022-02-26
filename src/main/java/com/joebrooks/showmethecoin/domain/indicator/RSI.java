package com.joebrooks.showmethecoin.domain.indicator;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RSI {
    private double rsi;
    private Recommend recommend;
}
