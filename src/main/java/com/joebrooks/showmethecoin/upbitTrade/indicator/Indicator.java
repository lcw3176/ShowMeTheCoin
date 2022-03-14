package com.joebrooks.showmethecoin.upbitTrade.indicator;

import com.joebrooks.showmethecoin.global.graph.GraphStatus;
import com.joebrooks.showmethecoin.upbitTrade.indicator.type.IndicatorType;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Indicator {

    private IndicatorType type;
    private double value;
    private GraphStatus status;
}
