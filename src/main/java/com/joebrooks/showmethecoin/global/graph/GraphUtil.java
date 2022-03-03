package com.joebrooks.showmethecoin.global.graph;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GraphUtil {

    public static GraphStatus getStatus(double firstValue, double secondValue){
        return secondValue - firstValue > 0 ?
                GraphStatus.RISING : GraphStatus.FALLING;
    }
}
