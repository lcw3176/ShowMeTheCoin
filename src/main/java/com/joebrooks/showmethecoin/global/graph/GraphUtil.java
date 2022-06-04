package com.joebrooks.showmethecoin.global.graph;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GraphUtil {

    public static GraphStatus getStatus(double firstValue, double secondValue){
        if(secondValue - firstValue > 0){
            if(secondValue - firstValue > 10){
                return GraphStatus.STRONG_RISING;
            }
            return GraphStatus.RISING;
        } else if(secondValue - firstValue < 0){
            if(secondValue - firstValue < -4){
                return GraphStatus.STRONG_FALLING;
            }
            return GraphStatus.FALLING;
        } else{
            return GraphStatus.STAY;
        }

    }
}
