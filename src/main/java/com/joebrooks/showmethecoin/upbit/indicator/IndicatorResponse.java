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
    // fixme 값 정리 필요
    private IndicatorType type;
    private double olderValue;
    private double recentValue;
    private double newestValue;
    private GraphStatus status;
}
