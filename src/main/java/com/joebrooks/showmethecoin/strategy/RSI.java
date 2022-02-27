package com.joebrooks.showmethecoin.strategy;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RSI {
    private double rsi;
    private RecommendAction recommend; // fixme 제거
}
