package com.joebrooks.showmethecoin.global.graph;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GraphUtil {

    public static GraphStatus getStatus(double firstValue, double secondValue){
        if(secondValue - firstValue > 0){
            if(secondValue - firstValue > 4){
                return GraphStatus.STRONG_RISING;
            } else {
                return GraphStatus.WEAK_RISING;
            }

        } else if(secondValue - firstValue < 0){
            return GraphStatus.FALLING;
        } else{
            return GraphStatus.STAY;
        }

    }
}
