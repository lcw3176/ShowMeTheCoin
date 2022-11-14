package com.joebrooks.showmethecoin.trade.backtest;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BackTestResponse {

    private Date time;
    private double open;
    private double high;
    private double low;
    private double close;

    private double tradedPrice;

    private boolean traded;
    private boolean buy;
    private boolean finish;
    private boolean load;

    private int percentage;
    private double gain;

}
