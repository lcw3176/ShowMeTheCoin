package com.joebrooks.showmethecoin.global.graph;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GraphUtil {

    public GraphStatus getStatus(double pastValue, double recentValue){
        if(recentValue - pastValue > 0){
            return GraphStatus.RISING;
        } else if(recentValue - pastValue < 0){
            return GraphStatus.FALLING;
        } else{
            return GraphStatus.STAY;
        }

    }
}
