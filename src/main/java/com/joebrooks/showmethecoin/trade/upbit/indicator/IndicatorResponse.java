package com.joebrooks.showmethecoin.trade.upbit.indicator;

import com.joebrooks.showmethecoin.global.graph.GraphStatus;
import com.joebrooks.showmethecoin.trade.upbit.indicator.type.IndicatorType;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class IndicatorResponse {

    private IndicatorType type;
    private List<Double> values;
    private GraphStatus status;
}
