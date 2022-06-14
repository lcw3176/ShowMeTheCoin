package com.joebrooks.showmethecoin.backtest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BackTestResponse {
    private String dateKst;
    private double startPrice;
    private double closePrice;
    private double lowPrice;
    private double highPrice;

    private double tradedPrice;

    private boolean traded;
    private boolean buy;
    private boolean finish;
    private double gain;


}
