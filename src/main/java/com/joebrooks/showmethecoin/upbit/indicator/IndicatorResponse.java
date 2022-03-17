package com.joebrooks.showmethecoin.upbit.indicator;

import com.joebrooks.showmethecoin.global.graph.GraphStatus;
import com.joebrooks.showmethecoin.upbit.indicator.type.IndicatorType;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class IndicatorResponse {

    private IndicatorType type;
    private double value;
    private double beforeValue;
    private GraphStatus status;
}
