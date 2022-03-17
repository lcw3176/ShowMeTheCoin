package com.joebrooks.showmethecoin.global.graph;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GraphUtil {

    public static GraphStatus getStatus(double firstValue, double secondValue){
        if(secondValue - firstValue > 0){
            return GraphStatus.RISING;
        } else if(secondValue - firstValue < 0){
            return GraphStatus.FALLING;
        } else{
            return GraphStatus.STAY;
        }

    }
}
