package com.joebrooks.showmethecoin.indicator;

import com.joebrooks.showmethecoin.global.graph.GraphStatus;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Indicator {

    private String name;
    private double value;
    private GraphStatus status;
}
