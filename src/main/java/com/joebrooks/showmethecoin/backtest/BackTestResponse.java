package com.joebrooks.showmethecoin.backtest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BackTestResponse {
    private String dateKst;
    private double tradePrice;
    private boolean trade;
    private boolean buy;
    private boolean finish;
    private double gain;
}
