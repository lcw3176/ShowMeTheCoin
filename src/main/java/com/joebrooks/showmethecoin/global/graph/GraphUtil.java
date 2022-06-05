package com.joebrooks.showmethecoin.global.graph;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GraphUtil {

    private final double threshold = 0.3;

    public static GraphStatus getStatus(double pastValue, double recentValue){
        double maxValue = Math.max(pastValue, recentValue);
        double minValue = Math.min(pastValue, recentValue);
        double value = maxValue - minValue;

        if(recentValue - pastValue > 0){
            double temp = value / minValue * 100;

            if(temp > threshold){
                return GraphStatus.STRONG_RISING;
            }

            return GraphStatus.RISING;
        } else if(recentValue - pastValue < 0){
            double temp = value / maxValue * 100;

            if(temp > threshold){
                return GraphStatus.STRONG_FALLING;
            }

            return GraphStatus.FALLING;
        } else{
            return GraphStatus.STAY;
        }

    }
}
