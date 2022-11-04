package com.joebrooks.showmethecoin.trade.backtest;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class BackTestResponse {

    private Date time;
    private double open;
    private double high;
    private double low;
    private double close;

    private double tradedPrice;

    private double upperBollingers;
    private double middleBollingers;
    private double lowerBollingers;

    private boolean traded;
    private boolean buy;
    private boolean finish;
    private boolean load;

    private int percentage;
    private double gain;


}
